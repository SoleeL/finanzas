package com.soleel.finanzas.domain.stats

import com.soleel.finanzas.core.model.Stat
import com.soleel.finanzas.core.model.Transaction
import com.soleel.finanzas.core.model.enums.TransactionTypeEnum
import com.soleel.finanzas.data.transaction.interfaces.ITransactionLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoryStatsUseCase @Inject constructor(
    private val transactionRepository: ITransactionLocalDataSource
) {
    operator fun invoke(): Flow<Pair<List<Stat>, List<Stat>>> = transactionRepository.getTransactions()
        .mapToCategoryStat()
}

private fun Flow<List<Transaction>>.mapToCategoryStat(): Flow<Pair<List<Stat>, List<Stat>>> {
    return this.map(transform = { transactions ->
        val statsByType: Map<TransactionTypeEnum, List<Stat>> = transactions
            .groupBy(keySelector = { it.type })
            .mapValues(transform = { (transactionType, transactionsByType) ->
                transactionsByType
                    .groupBy(keySelector = { it.category })
                    .map(transform = { (transactionCategory, transactionsByCategory) ->
                        Stat(
                            category = transactionCategory,
                            amount = transactionsByCategory.sumOf(selector = { transaction: Transaction ->
                                transaction.amount
                            }),
                            transactionNumber = transactionsByCategory.size
                        )
                    })
            })

        Pair<List<Stat>, List<Stat>>(
            statsByType[TransactionTypeEnum.EXPENDITURE].orEmpty(),
            statsByType[TransactionTypeEnum.INCOME].orEmpty()
        )
    })
}