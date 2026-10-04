package budgetpilot.appium.ui.dialogs

import budgetpilot.appium.common.helpers.step
import budgetpilot.appium.models.AccountCurrency
import budgetpilot.appium.models.AccountType
import budgetpilot.appium.ui.screens.AccountsScreen
import budgetpilot.appium.ui.screens.BaseScreen
import io.appium.java_client.android.AndroidDriver

class AccountEditorDialog(driver: AndroidDriver) : BaseScreen(driver) {
    private companion object {
        const val NAME_FIELD = "account_name_field"
        const val BALANCE_FIELD = "account_balance_field"
        const val CURRENCY_CHIP_PREFIX = "account_currency_chip_"
        const val SAVE_BUTTON_TEXT = "Save"
    }

    private val nameField = byEditTextFor(NAME_FIELD)
    private val balanceField = byEditTextFor(BALANCE_FIELD)
    private val saveButton = byText(SAVE_BUTTON_TEXT)

    fun enterName(walletName: String) = step("Enter wallet name '$walletName'") {
        driver.findElement(nameField).sendKeys(walletName)
    }

    fun enterBalance(balance: String) = step("Enter wallet balance '$balance'") {
        driver.findElement(balanceField).sendKeys(balance)
    }

    fun selectType(type: AccountType) = step("Select account type '${type.label}'") {
        driver.findElement(byText(type.label)).click()
    }

    fun selectCurrency(currency: AccountCurrency) =
        step("Select currency '${currency.code}'") {
            driver.findElement(
                byAccessibilityId("$CURRENCY_CHIP_PREFIX${currency.code.lowercase()}")
            ).click()
        }

    fun clickSave(): AccountsScreen = step("Click Save") {
        driver.findElement(saveButton).click()
        AccountsScreen(driver)
    }

    fun fillWalletCreationForm(
        walletName: String,
        balance: String,
        type: AccountType,
        currency: AccountCurrency
    ): AccountsScreen = step("Fill wallet creation form and save") {
        enterName(walletName)
        enterBalance(balance)
        selectType(type)
        selectCurrency(currency)
        clickSave()
    }
}
