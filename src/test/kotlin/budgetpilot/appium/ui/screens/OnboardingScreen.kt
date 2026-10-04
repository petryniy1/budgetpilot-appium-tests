package budgetpilot.appium.ui.screens

import budgetpilot.appium.common.helpers.step
import io.appium.java_client.android.AndroidDriver

class OnboardingScreen(driver: AndroidDriver) : BaseScreen(driver) {
    private companion object {
        const val NEXT_BUTTON_TEXT = "Next"
        const val START_USING_BUTTON_TEXT = "Start using BudgetPilot"
        const val FINISH_BUTTON_TEXT = "Finish"
    }

    fun completeFirstLaunchFlow(): AccountsScreen = step("Complete onboarding and guided tutorial") {
        repeat(2) {
            driver.findElement(byText(NEXT_BUTTON_TEXT)).click()
        }
        driver.findElement(byText(START_USING_BUTTON_TEXT)).click()
        repeat(9) {
            driver.findElement(byText(NEXT_BUTTON_TEXT)).click()
        }
        driver.findElement(byText(FINISH_BUTTON_TEXT)).click()
        AccountsScreen(driver)
    }
}
