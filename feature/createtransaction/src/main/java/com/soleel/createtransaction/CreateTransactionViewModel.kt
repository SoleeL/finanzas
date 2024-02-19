package com.soleel.createtransaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soleel.common.result.Result
import com.soleel.common.result.asResult
import com.soleel.common.retryflow.RetryableFlowTrigger
import com.soleel.common.retryflow.retryableFlow
import com.soleel.paymentaccount.interfaces.IPaymentAccountLocalDataSource
import com.soleel.paymentaccount.model.PaymentAccount
import com.soleel.transaction.interfaces.ITransactionLocalDataSource
import com.soleel.validation.validator.CategoryTypeValidator
import com.soleel.validation.validator.NameValidator
import com.soleel.validation.validator.PaymentAccountTypeValidator
import com.soleel.validation.validator.TransactionAmountValidator
import com.soleel.validation.validator.TransactionTypeValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class CreateTransactionUiCreate(
    val paymentAccount: PaymentAccount = PaymentAccount(
        id = "", name = "", amount = 0, createAt = 0, updatedAt = 0, accountType = 0
    ),
    val paymentAccountError: Int? = null,

    val transactionType: Int = 0,
    val transactionTypeError: Int? = null,

    val categoryType: Int = 0,
    val categoryTypeError: Int? = null,

    val name: String = "",
    val nameError: Int? = null,

    val amount: String = "",
    val amountError: Int? = null,

    val isTransactionSaved: Boolean = false
)

sealed class CreateTransactionUiEvent {
    data class NameChanged(val name: String) : CreateTransactionUiEvent()
    data class AmountChanged(val amount: String) : CreateTransactionUiEvent()
    data class CategoryTypeChanged(val categoryType: Int) : CreateTransactionUiEvent()
    data class TransactionTypeChanged(val transactionType: Int) : CreateTransactionUiEvent()
    data class PaymentAccountChanged(val paymentAccount: PaymentAccount) :
        CreateTransactionUiEvent()

    data object Submit : CreateTransactionUiEvent()
}

sealed interface PaymentAccountsUiState {
    data class Success(val paymentAccounts: List<PaymentAccount>) : PaymentAccountsUiState
    data object Error : PaymentAccountsUiState
    data object Loading : PaymentAccountsUiState
}

sealed class PaymentAccountsUiEvent {
    data object Retry : PaymentAccountsUiEvent()
}


@HiltViewModel
class CreateTransactionViewModel @Inject constructor(
    private val paymentAccountRepository: IPaymentAccountLocalDataSource,
    private val transactionRepository: ITransactionLocalDataSource,
    private val retryableFlowTrigger: RetryableFlowTrigger
) : ViewModel() {

    var createTransactionUiCreate by mutableStateOf(CreateTransactionUiCreate())

    private val nameValidator = NameValidator()
    private val amountValidator = TransactionAmountValidator()
    private val categoryTypeValidator = CategoryTypeValidator()
    private val transactionTypeValidator = TransactionTypeValidator()
    private val validatePaymentAccountUseCase = PaymentAccountTypeValidator()

    private val _paymentAccountsUiState: Flow<PaymentAccountsUiState> = retryableFlowTrigger
        .retryableFlow(flowProvider = {
            paymentAccountUiState(paymentAccountRepository = paymentAccountRepository)
        })

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

//    private fun paymentAccountUiState(
//        paymentAccountRepository: IPaymentAccountLocalDataSource,
//    ): Flow<PaymentAccountsUiState> {
//        return flow {
//            emit(PaymentAccountsUiState.Loading)
//
//            delay(2000)
//
//            val itemsPaymentAccount = paymentAccountRepository.getPaymentAccounts()
//                .asResult()
//                .map {  getData(it)}
//
//            emitAll(itemsPaymentAccount)
//        }
//    }

    private fun getData(
        itemsPaymentAccount: Result<List<PaymentAccount>>
    ): PaymentAccountsUiState {
        return when (itemsPaymentAccount) {
            is Result.Success -> PaymentAccountsUiState.Success(itemsPaymentAccount.data)
            is Result.Error -> PaymentAccountsUiState.Error
            is Result.Loading -> PaymentAccountsUiState.Loading
        }
    }

    fun onPaymentAccountsUiEvent(event: PaymentAccountsUiEvent) {
        when (event) {
            is PaymentAccountsUiEvent.Retry -> {
                retryableFlowTrigger.retry()
            }
        }
    }

    fun onCreateTransactionUiEvent(event: CreateTransactionUiEvent) {
        when (event) {
            is CreateTransactionUiEvent.PaymentAccountChanged -> {
                // README: Campo objetivo
                createTransactionUiCreate = createTransactionUiCreate.copy(
                    paymentAccount = event.paymentAccount
                )

                // README: Campos afectados
                createTransactionUiCreate = createTransactionUiCreate.copy(
                    transactionType = 0,
                    transactionTypeError = null,
                    categoryType = 0,
                    categoryTypeError = null
                )

                validatePaymentAccount()
            }

            is CreateTransactionUiEvent.TransactionTypeChanged -> {
                // README: Campo objetivo
                createTransactionUiCreate = createTransactionUiCreate.copy(
                    transactionType = event.transactionType
                )

                // README: Campos afectados
                createTransactionUiCreate = createTransactionUiCreate.copy(
                    categoryType = 0,
                    categoryTypeError = null
                )

                validateTransactionType()
            }

            is CreateTransactionUiEvent.CategoryTypeChanged -> {
                createTransactionUiCreate = createTransactionUiCreate.copy(
                    categoryType = event.categoryType
                )
                validateCategoryType()
            }

            is CreateTransactionUiEvent.NameChanged -> {
                createTransactionUiCreate = createTransactionUiCreate.copy(name = event.name)
                validateName()
            }

            is CreateTransactionUiEvent.AmountChanged -> {
                createTransactionUiCreate = createTransactionUiCreate.copy(amount = event.amount)
                validateAmount()
            }

            is CreateTransactionUiEvent.Submit -> {
                if (validatePaymentAccount()
                    && validateTransactionType()
                    && validateCategoryType()
                    && validateName()
                    && validateAmount()
                ) {
                    saveTransaction()
                }
            }
        }
    }

    private fun validatePaymentAccount(): Boolean {
        val paymentAccountResult = validatePaymentAccountUseCase.execute(
            input = createTransactionUiCreate.paymentAccount
        )
        createTransactionUiCreate = createTransactionUiCreate.copy(
            paymentAccountError = paymentAccountResult.errorMessage
        )
        return paymentAccountResult.successful
    }

    private fun validateTransactionType(): Boolean {
        val transactionTypeResult = transactionTypeValidator.execute(
            input = createTransactionUiCreate.transactionType
        )
        createTransactionUiCreate = createTransactionUiCreate.copy(
            transactionTypeError = transactionTypeResult.errorMessage
        )
        return transactionTypeResult.successful
    }

    private fun validateCategoryType(): Boolean {
        val categoryTypeResult = categoryTypeValidator.execute(
            input = createTransactionUiCreate.categoryType
        )
        createTransactionUiCreate = createTransactionUiCreate.copy(
            categoryTypeError = categoryTypeResult.errorMessage
        )
        return categoryTypeResult.successful
    }

    private fun validateName(): Boolean {
        val nameResult = nameValidator.execute(input = createTransactionUiCreate.name)
        createTransactionUiCreate = createTransactionUiCreate.copy(
            nameError = nameResult.errorMessage
        )
        return nameResult.successful
    }

    private fun validateAmount(): Boolean {
        val input = Triple<String, Int, Int>(
            first = createTransactionUiCreate.amount,
            second = createTransactionUiCreate.paymentAccount.amount,
            third = createTransactionUiCreate.transactionType
        )

        val amountResult = amountValidator.execute(input = input)

        createTransactionUiCreate = createTransactionUiCreate.copy(
            amountError = amountResult.errorMessage
        )
        return amountResult.successful
    }

    private fun saveTransaction() {
        viewModelScope.launch(
            context = Dispatchers.IO,
            block = {
                transactionRepository.createTransaction(
                    name = createTransactionUiCreate.name,
                    amount = createTransactionUiCreate.amount.toInt(),
                    transactionType = createTransactionUiCreate.transactionType,
                    categoryType = createTransactionUiCreate.categoryType,
                    paymentAccountId = createTransactionUiCreate.paymentAccount.id
                )

                createTransactionUiCreate = createTransactionUiCreate.copy(
                    isTransactionSaved = true
                )
            })
    }
}

