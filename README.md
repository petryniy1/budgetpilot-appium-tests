# BudgetPilot Appium Tests

<p align="center">
  <strong>A Page Object UI automation framework for the BudgetPilot Android app.</strong>
</p>

<p align="center">
  <a href="https://kotlinlang.org/"><img src="https://img.shields.io/badge/language-Kotlin-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin" /></a>
  <a href="https://appium.io/"><img src="https://img.shields.io/badge/automation-Appium-5C3AA5?logo=appium&logoColor=white" alt="Appium" /></a>
  <a href="https://developer.android.com/"><img src="https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white" alt="Android" /></a>
  <a href="https://junit.org/junit5/"><img src="https://img.shields.io/badge/test%20runner-JUnit%205-25A162?logo=junit5&logoColor=white" alt="JUnit 5" /></a>
  <a href="https://www.selenium.dev/"><img src="https://img.shields.io/badge/driver-Selenium%20WebDriver-43B02A?logo=selenium&logoColor=white" alt="Selenium WebDriver" /></a>
  <a href="https://allurereport.org/"><img src="https://img.shields.io/badge/reporting-Allure-FF6E00" alt="Allure" /></a>
  <a href="https://gradle.org/"><img src="https://img.shields.io/badge/build-Gradle-02303A?logo=gradle&logoColor=white" alt="Gradle" /></a>
  <img src="https://img.shields.io/badge/status-actively%20developed-2BBCFF" alt="Actively developed" />
</p>

This repository is a black-box UI test framework for [BudgetPilot](https://github.com/petryniy1/BudgetPilot), a personal finance tracker for Android. Tests drive a built APK through Appium exactly as a real user would — no access to BudgetPilot's own source or internals is required or used.

The framework is intentionally a separate Gradle project rather than a module inside BudgetPilot itself. Wiring a pure-JVM test module into an Android application's own build caused a real Kotlin Gradle plugin version conflict (`org.jetbrains.kotlin.jvm` declared with two different versions on the same classpath). Keeping the two independent means BudgetPilot stays buildable on any machine with zero knowledge of this project, and this project can evolve its own dependency versions freely.

## What it tests

Currently covered, end to end against a real emulator/device:

- **First launch** — the three-slide onboarding plus the ten-step guided tutorial.
- **Wallet creation** — name, balance, type (Cash / Bank account), and currency (PLN / USD / EUR) are entered, saved, and verified back from the UI (list row, snackbar, currency-group total).
- **Multi-wallet currency totals** — creating several wallets of different types in the same currency correctly sums into a single currency-group total, and every wallet's name is explicitly present in that total's summary line.

## Project structure

```
src/test/kotlin/budgetpilot/appium/
├── common/              # Infrastructure shared by everything else
│   ├── DriverFactory.kt       # Builds the AndroidDriver / UiAutomator2 session
│   └── helpers/
│       └── AllureStep.kt      # step() — wraps an action/read as an Allure report step
├── models/              # Domain enums mirrored from the app under test
│   └── AccountOptions.kt      # AccountType, AccountCurrency
├── testdata/            # Concrete input values a test needs
│   └── WalletTestData.kt
├── ui/
│   ├── screens/         # Page Objects — one class per screen
│   ├── dialogs/         # Page Objects — one class per dialog/editor
│   └── flows/           # Multi-step action+assert combinations across a Page Object
│                        # (e.g. "create a wallet, then assert it everywhere it should appear")
└── tests/
    ├── BaseAppiumTest.kt       # Shared @BeforeEach/@AfterEach: session lifecycle, onboarding, screenshots
    ├── CreateAccountTest.kt
    └── AccountTotalsTest.kt
```

Plain getters and single-step actions live on the Page Object itself (`screens/`, `dialogs/`). Anything that chains several of those together with assertions — the kind of thing a test would otherwise repeat — lives in `ui/flows/` instead, kept separate so it can be reused across test classes.

## How the locators were found

BudgetPilot's UI is almost entirely Jetpack Compose, which merges accessibility semantics in ways that are not always obvious from the Compose code itself. Rather than guessing, every non-trivial locator here was confirmed against a real `driver.pageSource` dump before being written:

- **Compose merges semantics nodes.** For text fields, the real `android.widget.EditText` turned out to be the *parent* of the node carrying the `content-desc`, not a child — `byEditTextFor()` in `BaseScreen` locates it via `//android.widget.EditText[.//*[@content-desc='...']]`.
- **Snackbars are checked structurally**, through `pane-title`/`live-region` attributes, instead of matching their message text — a locator built from the *expected* text could never detect an unexpected one, such as an error instead of a success message.
- **`By` locators are cached, resolved `WebElement`s are not.** Caching a resolved element risks a stale-element exception when a dialog's open animation hasn't settled yet; re-resolving through `driver.findElement()` on every call costs nothing extra, since it benefits from the 10s implicit wait configured in `DriverFactory`.

## Reporting

Every Page Object action and read is wrapped in an [Allure](https://allurereport.org/) step through the shared `step()` helper, so a failure shows exactly which UI interaction it happened at — not just which test method.

```bash
./gradlew allureServe
```

builds the report from the latest run and opens it in a browser.

## Getting started

Requirements:

- [Appium](https://appium.io/) server running, with the `uiautomator2` driver installed (`appium driver install uiautomator2`)
- `ANDROID_HOME` / `JAVA_HOME` configured, `adb` on `PATH`
- A running emulator or connected device, with [BudgetPilot](https://github.com/petryniy1/BudgetPilot) installed
- JDK 21 (configured via `kotlin { jvmToolchain(21) }`)

```bash
git clone https://github.com/petryniy1/budgetpilot-appium-tests.git
cd budgetpilot-appium-tests
./gradlew test
```

The Appium server URL and target device name can be overridden via system properties (`-Dappium.server.url=...`, `-Ddevice.name=...`); they default to `http://127.0.0.1:4723` and `emulator-5554`.

## Project status

This is a portfolio project built alongside [BudgetPilot](https://github.com/petryniy1/BudgetPilot) to demonstrate a Page Object-based Appium framework on a real, non-trivial Compose application — including the locator-discovery problems Compose actually produces, not a simplified demo app.

Planned next: a Page Object layer and test suite for BudgetPilot's income/expense operations (create, update, delete), covering balance recalculation and currency-total consistency the same way wallet creation is covered today.
