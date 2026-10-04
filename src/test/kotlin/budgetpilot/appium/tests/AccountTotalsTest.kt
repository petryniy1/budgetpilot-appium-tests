package budgetpilot.appium.tests

import budgetpilot.appium.common.helpers.step
import budgetpilot.appium.models.AccountCurrency
import budgetpilot.appium.testdata.twoWalletsOfCurrency
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class AccountTotalsTest : BaseAppiumTest() {
    @ParameterizedTest
    @EnumSource(AccountCurrency::class)
    fun createTwoWalletsSameCurrency_totalSumsBothBalances(currency: AccountCurrency) {
        val (firstWallet, secondWallet) = twoWalletsOfCurrency(currency)
        val expectedTotalText = "300.00 ${currency.code}"

        val afterFirstWallet = accountsScreen.clickAdd()
            .fillWalletCreationForm(
                firstWallet.name,
                firstWallet.balanceInput,
                firstWallet.type,
                firstWallet.currency
            )

        val afterSecondWallet = afterFirstWallet.clickAdd()
            .fillWalletCreationForm(
                secondWallet.name,
                secondWallet.balanceInput,
                secondWallet.type,
                secondWallet.currency
            )

        step("Assert total for $currency sums both wallets and lists both names") {
            val walletNamesLine = afterSecondWallet
                .walletNamesLineInTotal(firstWallet.name)
            assertTrue(
                walletNamesLine
                    .contains(firstWallet.name)
            ) {
                "Expected the currency-group summary to contain '${firstWallet.name}'," +
                        " but was: '$walletNamesLine'"
            }
            assertTrue(
                walletNamesLine
                    .contains(secondWallet.name)
            ) {
                "Expected the currency-group summary to also contain '${secondWallet.name}'," +
                        " but was: '$walletNamesLine'"
            }

            assertEquals(
                expectedTotalText,
                afterSecondWallet.totalMatching(expectedTotalText)
            )
            assertEquals(
                currency.code,
                afterSecondWallet.totalCurrencyLabel(currency.code)
            )
        }
    }
}
