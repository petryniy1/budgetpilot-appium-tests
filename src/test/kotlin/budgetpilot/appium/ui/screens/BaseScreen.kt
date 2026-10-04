package budgetpilot.appium.ui.screens

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.By

abstract class BaseScreen(protected val driver: AndroidDriver) {
    protected fun byText(text: String, instance: Int? = null): By =
        if (instance != null) {
            AppiumBy.androidUIAutomator("new UiSelector().text(\"$text\").instance($instance)")
        } else {
            AppiumBy.androidUIAutomator("new UiSelector().text(\"$text\")")
        }

    protected fun byTextContains(text: String, instance: Int? = null): By =
        if (instance != null) {
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"$text\").instance($instance)")
        } else {
            AppiumBy.androidUIAutomator("new UiSelector().textContains(\"$text\")")
        }

    protected fun byAccessibilityId(id: String): By =
        AppiumBy.accessibilityId(id)

    protected fun byEditTextFor(accessibilityId: String): By =
        AppiumBy.xpath("//android.widget.EditText[.//*[@content-desc='$accessibilityId']]")

    protected fun byPaneTitle(paneTitle: String): By =
        AppiumBy.xpath("//*[@pane-title='$paneTitle']//android.widget.TextView")
}
