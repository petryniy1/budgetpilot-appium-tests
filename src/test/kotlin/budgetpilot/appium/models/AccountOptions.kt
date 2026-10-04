package budgetpilot.appium.models

enum class AccountType(val label: String) {
    CASH("Cash"),
    BANK_ACCOUNT("Bank account")
}

enum class AccountCurrency(val code: String) {
    PLN("PLN"),
    USD("USD"),
    EUR("EUR")
}