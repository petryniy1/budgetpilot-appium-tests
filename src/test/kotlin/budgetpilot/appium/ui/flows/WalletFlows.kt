package budgetpilot.appium.ui.flows

import budgetpilot.appium.common.helpers.step
import budgetpilot.appium.models.AccountCurrency
import budgetpilot.appium.models.AccountType
import budgetpilot.appium.ui.screens.AccountsScreen
import org.junit.jupiter.api.Assertions.assertEquals

private const val ACCOUNT_CREATED_MESSAGE = "Account created"

fun AccountsScreen.assertNewWalletParameters(
    walletName: String,
    walletBalanceInput: String,
    walletType: AccountType,
    walletCurrency: AccountCurrency,
    expectedBalanceText: String
) = step(
    "Assert wallet '$walletName' created with balance '$walletBalanceInput' in $walletCurrency"
) {
    assertEquals(ACCOUNT_CREATED_MESSAGE, snackbarText())
    assertEquals(walletName, this.walletName(walletName))
    assertEquals(walletType.label, this.walletType(walletType))

    val balanceText = walletBalance(walletName)
    assertEquals(expectedBalanceText, balanceText)
    assertEquals(balanceText, totalMatching(balanceText))
    assertEquals(walletName, walletNameInTotal(walletName))
    assertEquals(walletCurrency.code, totalCurrencyLabel(walletCurrency.code))
}
