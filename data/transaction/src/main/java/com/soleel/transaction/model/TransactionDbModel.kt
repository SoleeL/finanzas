package com.soleel.transaction.model

import com.soleel.database.entities.TransactionEntity


class TransactionDbModel {
    companion object {

        fun asExternalModel(transactionEntity: TransactionEntity): Transaction {
            return Transaction(
                id = transactionEntity.id,
                name = transactionEntity.name,
                amount = transactionEntity.amount,
                createAt = transactionEntity.createAt,
                updatedAt = transactionEntity.updatedAt,
                transactionType = transactionEntity.transactionType,
                categoryType = transactionEntity.categoryType,
                paymentAccountId = transactionEntity.paymentAccountId
            )
        }

        fun asExternalModelList(transactionEntities: List<TransactionEntity>): List<Transaction> {
            return transactionEntities.map(::asExternalModel)
        }
    }
}