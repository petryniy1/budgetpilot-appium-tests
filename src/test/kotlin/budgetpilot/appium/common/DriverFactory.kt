package budgetpilot.appium.common

import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import java.net.URI
import java.time.Duration

object DriverFactory {
    private const val APP_PACKAGE = "com.petryniy1.budgetpilot"
    private const val APP_ACTIVITY = "com.petryniy1.budgetpilot.presentation.main.MainActivity"

    fun createDriver(fullReset: Boolean = false): AndroidDriver {
        val serverUrl = System.getProperty("appium.server.url",
            "http://127.0.0.1:4723")
        val deviceName = System.getProperty("device.name", "emulator-5554")

        val options = UiAutomator2Options()
            .setPlatformName("Android")
            .setAutomationName("UiAutomator2")
            .setDeviceName(deviceName)
            .setAppPackage(APP_PACKAGE)
            .setAppActivity(APP_ACTIVITY)
            .setFullReset(fullReset)
            .setNoReset(!fullReset)
            .setNewCommandTimeout(Duration.ofSeconds(120))

        val driver = AndroidDriver(URI(serverUrl)
            .toURL(), options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30))

        return driver
    }
}
