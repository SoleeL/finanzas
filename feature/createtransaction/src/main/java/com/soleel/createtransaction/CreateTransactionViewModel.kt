package com.soleel.createtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soleel.common.createuistate.CreateUiState
import com.soleel.common.result.Result
import com.soleel.common.result.asResult
import com.soleel.common.createuistate.updateUserMessage
import com.soleel.paymentaccount.interfaces.IPaymentAccountLocalDataSource
import com.soleel.paymentaccount.model.PaymentAccount
import com.soleel.transaction.interfaces.ITransactionLocalDataSource
import com.soleel.transaction.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


//data class CreateTransactionUiState(
//    val name: String? = null,
//    val amount: Int? = null,
//    val description: String? = null,
//    val categoryType: Int? = null,
//    val transactionType: Int? = null,
//    val paymentAccountId: Int? = null,
//
//    val userMessage: String? = null,
//    val isTransactionSaved: Boolean = false
//)

sealed interface CreateTransactionUiState : CreateUiState {
    data object Success : CreateTransactionUiState
    data object Error : CreateTransactionUiState
    data class Loading(
        val transaction: Transaction,
        val userMessage: String?
    ) : CreateTransactionUiState
}

sealed interface PaymentAccountsUiState {
    data class Success(val paymentAccounts: List<PaymentAccount>) : PaymentAccountsUiState
    data object Error : PaymentAccountsUiState
    data object Loading : PaymentAccountsUiState
}

@HiltViewModel
class CreateTransactionViewModel @Inject constructor(
    private val paymentAccountRepository: IPaymentAccountLocalDataSource,
    private val transactionRepository: ITransactionLocalDataSource
) : ViewModel() {

//    private val _createTransactionUiState: MutableStateFlow<CreateTransactionUiState> =
//        MutableStateFlow(CreateTransactionUiState())
//    val createTransactionUiState: StateFlow<CreateTransactionUiState> =
//        _createTransactionUiState.asStateFlow()

    private val _createTransactionUiState =
        MutableStateFlow<CreateTransactionUiState>(
            CreateTransactionUiState.Loading(
                transaction = Transaction(
                    id = "",
                    name = "",
                    amount = 0,
                    description = "",
                    createAt = 0,
                    updatedAt = 0,
                    categoryType = 0,
                    transactionType = 0,
                    paymentAccountId = ""
                ),
                userMessage = null
            )
        )
    val createTransactionUiState: StateFlow<CreateTransactionUiState> =
        _createTransactionUiState.asStateFlow()

    private val _paymentAccountsUiState: Flow<PaymentAccountsUiState> = paymentAccountUiState(
        paymentAccountRepository = paymentAccountRepository
    )
    val paymentAccountsUiState: StateFlow<PaymentAccountsUiState> = _paymentAccountsUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = PaymentAccountsUiState.Loading
    )

    private fun paymentAccountUiState(
        paymentAccountRepository: IPaymentAccountLocalDataSource,
    ): Flow<PaymentAccountsUiState> {
        return paymentAccountRepository.getPaymentAccounts()
            .asResult()
            .map(transform = this::getData)
    }

    private fun getData(
        itemsPaymentAccount: Result<List<PaymentAccount>>
    ): PaymentAccountsUiState {
        return when (itemsPaymentAccount) {
            is Result.Success -> PaymentAccountsUiState.Success(itemsPaymentAccount.data)
            is Result.Loading -> PaymentAccountsUiState.Loading
            is Result.Error -> PaymentAccountsUiState.Error
        }
    }

    fun saveTransaction() {

        val transaction =
            (_createTransactionUiState.value as CreateTransactionUiState.Loading).transaction

        if (transaction.name.isEmpty()) {
            _createTransactionUiState.update(
                function = {
                    updateUserMessage(it, "Nombre no puede estar vacío")
                }
            )
        }

        // TODO: Cambiar 9999999 por el monto del PaymentAccount seleccionado
        if (transaction.amount >= 9999999) {
            _createTransactionUiState.update(
                function = {
                    updateUserMessage(it, "Monto mayor al disponible en la cuenta de pago")
                }
            )
        }

        if (transaction.amount <= 0) {
            _createTransactionUiState.update(
                function = {
                    updateUserMessage(it, "Monto inferior a 0")
                }
            )
        }

    }

//    private fun updateUserMessage(
//        createTransactionUiState: CreateTransactionUiState,
//        userMessage: String?
//    ): CreateTransactionUiState {
//        return when (createTransactionUiState) {
//            is CreateTransactionUiState.Loading -> createTransactionUiState.copy(userMessage = userMessage)
//            else -> createTransactionUiState
//        }
//    }

}

