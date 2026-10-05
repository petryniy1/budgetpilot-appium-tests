package budgetpilot.appium.testdata

import budgetpilot.appium.models.AccountCurrency
import budgetpilot.appium.models.AccountType

data class WalletInput(
    val name: String,
    val balanceInput: String,
    val type: AccountType,
    val currency: AccountCurrency
)

fun transferFromGiaWallet(currency: AccountCurrency = AccountCurrency.USD) = WalletInput(
    name = "Transfer from Gia",
    balanceInput = "1350",
    type = AccountType.BANK_ACCOUNT,
    currency = currency
)

fun twoWalletsOfCurrency(currency: AccountCurrency): Pair<WalletInput, WalletInput> =
    WalletInput(name = "Wallet One", balanceInput = "100", type = AccountType.CASH, currency = currency) to
        WalletInput(name = "Wallet Two", balanceInput = "200", type = AccountType.BANK_ACCOUNT, currency = currency)
