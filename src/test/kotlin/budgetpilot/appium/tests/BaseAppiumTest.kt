package budgetpilot.appium.tests

import budgetpilot.appium.common.DriverFactory
import budgetpilot.appium.ui.screens.AccountsScreen
import budgetpilot.appium.ui.screens.OnboardingScreen
import io.appium.java_client.android.AndroidDriver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.openqa.selenium.OutputType
import java.io.File

abstract class BaseAppiumTest {
    protected lateinit var driver: AndroidDriver
    protected lateinit var accountsScreen: AccountsScreen

    @BeforeEach
    fun setUp() {
        ProcessBuilder("adb", "shell", "pm", "clear", "com.petryniy1.budgetpilot")
            .start()
            .waitFor()
        driver = DriverFactory.createDriver()
        accountsScreen = OnboardingScreen(driver).completeFirstLaunchFlow()
    }

    @AfterEach
    fun tearDown(testInfo: TestInfo) {
        val screenshotFile = driver.getScreenshotAs(OutputType.FILE)
        val targetFile =
            File("screenshots/${testInfo.displayName}_${System.currentTimeMillis()}.png")
        targetFile.parentFile.mkdirs()
        screenshotFile.copyTo(targetFile)

        driver.quit()
    }
}
