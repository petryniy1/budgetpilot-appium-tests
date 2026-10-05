package budgetpilot.appium.tests

import budgetpilot.appium.testdata.transferFromGiaWallet
import budgetpilot.appium.ui.flows.assertNewWalletParameters
import org.junit.jupiter.api.Test

class CreateAccountTest : BaseAppiumTest() {
    @Test
    fun createAccount_showsCorrectBalanceAndCurrency() {
        val wallet = transferFromGiaWallet()
        val expectedBalanceText = "1 350.00 USD"

        val accountsScreenAfterSave = accountsScreen.clickAdd()
            .fillWalletCreationForm(wallet.name, wallet.balanceInput, wallet.type, wallet.currency)

        accountsScreenAfterSave.assertNewWalletParameters(
            wallet.name,
            wallet.balanceInput,
            wallet.type,
            wallet.currency,
            expectedBalanceText
        )
    }
}
