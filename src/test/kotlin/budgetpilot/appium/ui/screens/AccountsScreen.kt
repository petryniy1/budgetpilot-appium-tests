package budgetpilot.appium.ui.screens

import budgetpilot.appium.common.helpers.step
import budgetpilot.appium.models.AccountType
import budgetpilot.appium.ui.dialogs.AccountEditorDialog
import io.appium.java_client.android.AndroidDriver

class AccountsScreen(driver: AndroidDriver) : BaseScreen(driver) {
    private companion object {
        const val ADD_BUTTON_ID = "accounts_add_button"
        const val BALANCE_PREFIX = "account_balance_value_"
        const val SNACKBAR_PANE_TITLE = "Alert"
    }

    private val addButton = byAccessibilityId(ADD_BUTTON_ID)
    private val snackbarMessage = byPaneTitle(SNACKBAR_PANE_TITLE)

    fun walletName(walletName: String): String =
        step("Read wallet name '$walletName' from the row") {
            driver.findElement(byText(walletName, instance = 1)).text
        }

    fun walletBalance(walletName: String): String =
        step("Read balance for wallet '$walletName'") {
            driver.findElement(
                byAccessibilityId("$BALANCE_PREFIX$walletName")
            ).text
        }

    fun walletType(type: AccountType): String =
        step("Read wallet type '${type.label}'") {
            driver.findElement(byText(type.label)).text
        }

    fun totalMatching(balanceText: String): String =
        step("Read currency-group total matching '$balanceText'") {
            driver.findElement(byText(balanceText, instance = 0)).text
        }

    fun walletNameInTotal(walletName: String): String =
        step("Read wallet name '$walletName' from the currency-group total") {
            driver.findElement(byText(walletName, instance = 0)).text
        }

    fun totalCurrencyLabel(currencyCode: String): String =
        step("Read currency-group header label '$currencyCode'") {
            driver.findElement(byText(currencyCode)).text
        }

    fun walletNamesLineInTotal(walletName: String): String =
        step("Read currency-group wallet-names line containing '$walletName'") {
            driver.findElement(byTextContains(walletName)).text
        }

    fun clickAdd(): AccountEditorDialog =
        step("Click Add button on Accounts screen") {
            driver.findElement(addButton).click()
            AccountEditorDialog(driver)
        }

    fun snackbarText(): String = step("Read current snackbar message") {
        driver.findElement(snackbarMessage).text
    }
}
