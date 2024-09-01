package com.soleel.finanzas.domain.stats

import com.soleel.finanzas.core.model.Stat
import com.soleel.finanzas.core.model.Transaction
import com.soleel.finanzas.core.model.enums.TransactionTypeEnum
import com.soleel.finanzas.data.transaction.interfaces.ITransactionLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGeneralStatsUseCase @Inject constructor(
    private val transactionRepository: ITransactionLocalDataSource
) {
    operator fun invoke(): Flow<List<Stat>> =
        transactionRepository.getTransactions()
            .mapToGeneralStat()
}

private fun Flow<List<Transaction>>.mapToGeneralStat(): Flow<List<Stat>> {
    return this.map(transform = { transactions ->
        val transactionsByType: Map<TransactionTypeEnum, List<Transaction>> =
            transactions.groupBy(keySelector = { it.type })

        val expenditureTransactions: List<Transaction> =
            transactionsByType[TransactionTypeEnum.EXPENDITURE] ?: emptyList()
        val expenditureGeneralStat: Stat = Stat(
            type = TransactionTypeEnum.EXPENDITURE,
            amount = expenditureTransactions.sumOf(selector = { transaction: Transaction ->
                transaction.amount
            }),
            transactionNumber = expenditureTransactions.size
        )

        val incomeTransactions: List<Transaction> =
            transactionsByType[TransactionTypeEnum.INCOME] ?: emptyList()
        val incomeGeneralStat: Stat = Stat(
            type = TransactionTypeEnum.INCOME,
            amount = incomeTransactions.sumOf(selector = { transaction: Transaction ->
                transaction.amount
            }),
            transactionNumber = incomeTransactions.size
        )

        val balanceGeneralStat: Stat = Stat(
            amount = incomeGeneralStat.amount - expenditureGeneralStat.amount
        )

        listOf(
            expenditureGeneralStat,
            balanceGeneralStat,
            incomeGeneralStat
        )
    })
}

