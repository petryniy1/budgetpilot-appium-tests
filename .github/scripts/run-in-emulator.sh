#!/usr/bin/env bash
set -uo pipefail

# Runs everything that needs the booted emulator: build + install the app
# under test, start Appium, run this repo's test suite, and build the Allure
# report regardless of whether the tests passed. Called as a single command
# from the android-emulator-runner "script:" step, because each line of that
# step's own YAML value runs as an independent shell (no persisted cd/env
# between lines) - doing it all inside one real script file avoids that.

cd BudgetPilot || exit 1
chmod +x gradlew
./gradlew assembleDebug || exit 1
adb install -r app/build/outputs/apk/debug/app-debug.apk || exit 1
cd ..

npm install -g appium@3.7.0
appium driver install uiautomator2

appium --log-timestamp &
APPIUM_PID=$!

echo "Waiting for Appium to come up..."
for i in $(seq 1 30); do
  if curl -sf http://127.0.0.1:4723/status > /dev/null; then
    echo "Appium is up."
    break
  fi
  sleep 2
done

chmod +x gradlew
./gradlew test -Dappium.server.url=http://127.0.0.1:4723 -Ddevice.name=emulator-5554
TEST_EXIT=$?

./gradlew allureReport

kill "$APPIUM_PID" 2>/dev/null || true

exit $TEST_EXIT
