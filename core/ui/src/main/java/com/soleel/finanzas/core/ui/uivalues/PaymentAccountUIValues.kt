package com.soleel.finanzas.core.ui.uivalues

import com.soleel.finanzas.core.common.enums.PaymentAccountTypeEnum


data class PaymentAccountUIValues(
    val type: PaymentAccountTypeUIValues,
    var name: String,
    var amount: String,
)

fun getPaymentAccountUI(
    paymentAccountTypeEnum: PaymentAccountTypeEnum,
    paymentAccountName: String,
    paymentAccountAmount: String
): PaymentAccountUIValues {
    val paymentAccountTypeUI: PaymentAccountTypeUIValues = getPaymentAccountTypeUI(
        paymentAccountType = paymentAccountTypeEnum
    )

    val paymentAccountUI: PaymentAccountUIValues = PaymentAccountUIValues(
        type = paymentAccountTypeUI,
        name = paymentAccountName,
        amount = paymentAccountAmount
    )

    return paymentAccountUI
}