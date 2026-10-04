package budgetpilot.appium.common.helpers

import io.qameta.allure.Allure

fun <T> step(title: String, action: () -> T): T =
    Allure.step(title, Allure.ThrowableRunnable<T> { action() })
