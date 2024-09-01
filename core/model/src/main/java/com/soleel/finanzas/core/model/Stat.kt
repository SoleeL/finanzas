package com.soleel.finanzas.core.model

import com.soleel.finanzas.core.model.enums.TransactionCategoryEnum
import com.soleel.finanzas.core.model.enums.TransactionTypeEnum

data class Stat(
    val type: TransactionTypeEnum? = null,
    val category: TransactionCategoryEnum? = null,
    val amount: Int,
    val transactionNumber: Int? = null
)