package com.soleel.finanzas.feature.createtransaction

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.soleel.finanzas.core.common.enums.AccountTypeEnum
import com.soleel.finanzas.core.common.enums.TransactionCategoryEnum
import com.soleel.finanzas.core.common.enums.TransactionTypeEnum
import com.soleel.finanzas.core.model.Account
import com.soleel.finanzas.core.ui.R
import com.soleel.finanzas.core.ui.template.CancelAlertDialog
import com.soleel.finanzas.core.ui.template.CreateTopAppBar
import com.soleel.finanzas.core.ui.template.LargeDropdownMenu
import com.soleel.finanzas.domain.transformation.visualtransformation.CurrencyVisualTransformation
import com.soleel.finanzas.domain.validation.validator.ValidatorTransactionAmount
import java.util.Date

@Composable
internal fun CreateTransactionRoute(
    modifier: Modifier = Modifier,
    onBackToPreviousView: () -> Unit,
    viewModel: CreateTransactionViewModel = hiltViewModel()
) {
    val accountsUiState by viewModel.accountsUiState.collectAsStateWithLifecycle()

    val createTransactionUiState: CreateTransactionUiState = viewModel.createTransactionUiState

    TransactionCreateScreen(
        modifier = modifier,
        onBackToPreviousView = onBackToPreviousView,
        accountsUiState = accountsUiState,
        onAccountsUiEvent = viewModel::onAccountsUiEvent,
        createTransactionUiState = createTransactionUiState,
        onCreateTransactionUiEvent = viewModel::onCreateTransactionUiEvent
    )
}

@Preview
@Composable
private fun TransactionCreateScreenPreview() {
    TransactionCreateScreen(
        modifier = Modifier,
        onBackToPreviousView = {},
        accountsUiState = AccountsUiState.Success(
            accounts = listOf(
                Account(
                    id = "1",
                    name = "CMR Falabella",
                    amount = 240000,
                    createAt = Date(),
                    updatedAt = Date(),
                    type = AccountTypeEnum.CREDIT,
                ),
                Account(
                    id = "2",
                    name = "Falabella debito",
                    amount = 100000,
                    createAt = Date(),
                    updatedAt = Date(),
                    type = AccountTypeEnum.DEBIT,
                ),
                Account(
                    id = "3",
                    name = "Cuenta rut",
                    amount = 100000,
                    createAt = Date(),
                    updatedAt = Date(),
                    type = AccountTypeEnum.DEBIT,
                ),
                Account(
                    id = "4",
                    name = "Racional app",
                    amount = 9000000,
                    createAt = Date(),
                    updatedAt = Date(),
                    type = AccountTypeEnum.SAVING,
                )
            )
        ),
        onAccountsUiEvent = {},
        createTransactionUiState = CreateTransactionUiState(),
        onCreateTransactionUiEvent = {}
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun TransactionCreateScreen(
    modifier: Modifier,
    onBackToPreviousView: () -> Unit,
    accountsUiState: AccountsUiState,
    onAccountsUiEvent: (AccountsUiEvent) -> Unit,
    createTransactionUiState: CreateTransactionUiState,
    onCreateTransactionUiEvent: (CreateTransactionUiEvent) -> Unit
) {
    val externalBackHandler: MutableState<Boolean> =
        remember(calculation = { mutableStateOf(true) })

    BackHandler(enabled = externalBackHandler.value, onBack = onBackToPreviousView)

    Scaffold(
        modifier = modifier,
        topBar = {
            CreateTopAppBar(
                title = R.string.trasaction_create_title,
                onBackButton = onBackToPreviousView
            )
        },
        bottomBar = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Button(
                        onClick = { onCreateTransactionUiEvent(CreateTransactionUiEvent.Submit) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(64.dp),
                        enabled = createTransactionUiState.account.id.isNotBlank() &&
                                0 != createTransactionUiState.transactionType &&
                                0 != createTransactionUiState.transactionCategory &&
                                createTransactionUiState.transactionName.isNotBlank() &&
                                0 != createTransactionUiState.transactionAmount,
                        content = { Text(text = stringResource(id = R.string.save_transaction_button)) }
                    )
                }
            )
        },
        content = {
            when (accountsUiState) {
                is AccountsUiState.Success -> {
                    externalBackHandler.value = false
                    TransactionCreateSuccess(
                        modifier = Modifier.padding(it),
                        onBackToPreviousView = onBackToPreviousView,
                        accounts = accountsUiState.accounts,
                        createTransactionUiState = createTransactionUiState,
                        onCreateTransactionUiEvent = onCreateTransactionUiEvent
                    )
                }

                is AccountsUiState.Error -> TransactionCreateErrorScreen(
                    modifier = modifier,
                    onRetry = { onAccountsUiEvent(AccountsUiEvent.Retry) }
                )

                is AccountsUiState.Loading -> TransactionCreateLoadingScreen()
            }
        }
    )
}

@Composable
fun TransactionCreateSuccess(
    modifier: Modifier,
    onBackToPreviousView: () -> Unit,
    accounts: List<Account>,
    createTransactionUiState: CreateTransactionUiState,
    onCreateTransactionUiEvent: (CreateTransactionUiEvent) -> Unit
) {
    val showCancelAlert: MutableState<Boolean> = remember(calculation = { mutableStateOf(false) })

    if (showCancelAlert.value) {
        CancelAlertDialog(
            onDismiss = { showCancelAlert.value = false },
            onConfirmation = {
                showCancelAlert.value = false
                onBackToPreviousView()
            },
            dialogTitle = "¿Quieres volver atras?",
            dialogText = "Cancelaras la creacion de esta transaccion."
        )
    }

    BackHandler(enabled = true, onBack = { showCancelAlert.value = false == showCancelAlert.value })

    if (createTransactionUiState.isTransactionSaved) {
        onBackToPreviousView()
    }

    val currencyVisualTransformation by remember(calculation = {
        mutableStateOf(CurrencyVisualTransformation(currencyCode = "USD"))
    })

    Column(
        modifier = modifier,
        content = {

            SelectAccountDropdownMenu(
                accounts = accounts,
                onCreateTransactionUiEvent = onCreateTransactionUiEvent,
                currencyVisualTransformation = currencyVisualTransformation
            )

            Spacer(modifier = Modifier.height(16.dp))

            SelectTransactionTypeDropdownMenu(
                createTransactionUiState = createTransactionUiState,
                onCreateTransactionUiEvent = onCreateTransactionUiEvent
            )

            Spacer(modifier = Modifier.height(16.dp))

            SelectTransactionCategoryDropdownMenu(
                createTransactionUiState = createTransactionUiState,
                onCreateTransactionUiEvent = onCreateTransactionUiEvent
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputTransactionNameTextField(
                createTransactionUiState = createTransactionUiState,
                onCreateTransactionUiEvent = onCreateTransactionUiEvent
            )

            InputTransactionAmountTextField(
                createTransactionUiState = createTransactionUiState,
                onCreateTransactionUiEvent = onCreateTransactionUiEvent,
                currencyVisualTransformation = currencyVisualTransformation
            )
        }
    )
}

@Composable
fun SelectAccountDropdownMenu(
    accounts: List<Account>,
    onCreateTransactionUiEvent: (CreateTransactionUiEvent) -> Unit,
    currencyVisualTransformation: CurrencyVisualTransformation
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        content = {
            LargeDropdownMenu(
                label = "Cuenta de pago",
                items = accounts,
                selectedIndex = selectedIndex,
                onItemSelected = { index, account ->
                    selectedIndex = index
                    onCreateTransactionUiEvent(CreateTransactionUiEvent.AccountChanged(account))
                },
                selectedItemToStartString = { account: Account ->
                    "${account.name} - ${account.type.value} "
                },
                withEndText = true,
                selectedItemToEndString = { account: Account ->
                    currencyVisualTransformation
                        .filter(AnnotatedString(text = account.amount.toString()))
                        .text
                        .toString()
                }
            )
        }
    )
}

@Composable
fun SelectTransactionTypeDropdownMenu(
    createTransactionUiState: CreateTransactionUiState,
    onCreateTransactionUiEvent: (CreateTransactionUiEvent) -> Unit,
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        content = {
            LargeDropdownMenu(
                enabled = createTransactionUiState.account.id.isNotEmpty(),
                label = "Tipo de transaccion",
                items = TransactionTypeEnum.entries,
                selectedIndex = selectedIndex,
                onItemSelected = { index, transactionType ->
                    selectedIndex = index
                    onCreateTransactionUiEvent(
                        CreateTransactionUiEvent.TransactionTypeChanged(
                            transactionType.id
                        )
                    )
                },
                selectedItemToStartString = { transactionType: TransactionTypeEnum ->
                    transactionType.value
                }
            )
        }
    )
}

@Composable
fun SelectTransactionCategoryDropdownMenu(
    createTransactionUiState: CreateTransactionUiState,
    onCreateTransactionUiEvent: (CreateTransactionUiEvent) -> Unit,
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        content = {
            LargeDropdownMenu(
                enabled = 0 != createTransactionUiState.transactionType,
                label = "Categoria de transaccion",
                items = TransactionCategoryEnum.getTransactionCategories(
                    transactionType = TransactionTypeEnum.fromId(createTransactionUiState.transactionType),
                    accountType = AccountTypeEnum.fromId(createTransactionUiState.account.type.id)
                ),
                selectedIndex = selectedIndex,
                onItemSelected = { index, transactionCategory ->
                    selectedIndex = index
                    onCreateTransactionUiEvent(
                        CreateTransactionUiEvent.TransactionCategoryChanged(
                            transactionCategory.id
                        )
                    )
                },
                selectedItemToStartString = { transactionCategory: TransactionCategoryEnum ->
                    transactionCategory.value
                }
            )
        }
    )
}

@Composable
fun InputTransactionNameTextField(
    createTransactionUiState: CreateTransactionUiState,
    onCreateTransactionUiEvent: (CreateTransactionUiEvent) -> Unit
) {
    OutlinedTextField(
        value = createTransactionUiState.transactionName,
        onValueChange = {
            onCreateTransactionUiEvent(
                CreateTransactionUiEvent.TransactionNameChanged(
                    it
                )
            )
        },
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxWidth(),
        enabled = 0 != createTransactionUiState.transactionCategory,
        label = { Text(text = stringResource(id = R.string.attribute_trasaction_name_field)) },
        supportingText = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (createTransactionUiState.transactionNameError == null)
                    stringResource(id = R.string.required_field) else
                    stringResource(id = createTransactionUiState.transactionNameError),
                textAlign = TextAlign.End,
            )
        },
        trailingIcon = {
            if (createTransactionUiState.transactionNameError != null) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    tint = Color.Red, // Cambiar color
                    contentDescription = "Nombre de la transaccion a crear"
                )
            }
        },
        isError = createTransactionUiState.transactionNameError != null,
        singleLine = true
    )
}

@Composable
fun InputTransactionAmountTextField(
    createTransactionUiState: CreateTransactionUiState,
    onCreateTransactionUiEvent: (CreateTransactionUiEvent) -> Unit,
    currencyVisualTransformation: CurrencyVisualTransformation
) {
    OutlinedTextField(
        value = if (0 != createTransactionUiState.transactionAmount) createTransactionUiState.transactionAmount.toString() else "",
        onValueChange = { input: String ->
            val trimmed = input
                .trimStart('0')
                .trim(predicate = { inputTrimStart -> inputTrimStart.isDigit().not() })

            if (trimmed.length <= ValidatorTransactionAmount.MAX_CHAR_LIMIT) {
                onCreateTransactionUiEvent(
                    CreateTransactionUiEvent.TransactionAmountChanged(
                        if (trimmed.isBlank()) 0 else trimmed.toInt()
                    )
                )
            }
        },
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxWidth(),
        enabled = 0 != createTransactionUiState.transactionCategory,
        label = { Text(text = stringResource(id = R.string.attribute_trasaction_amount_field)) },
        trailingIcon = {
            if (null != createTransactionUiState.transactionAmountError) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    tint = Color.Red, // Cambiar color
                    contentDescription = "Monto de la transaccion a crear"
                )
            }
        },
        supportingText = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (null == createTransactionUiState.transactionAmountError)
                    stringResource(id = R.string.required_field) else
                    stringResource(id = createTransactionUiState.transactionAmountError),
                textAlign = TextAlign.End,
            )
        },
        isError = createTransactionUiState.transactionAmountError != null,
        visualTransformation = currencyVisualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@Composable
fun TransactionCreateErrorScreen(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Error de carga",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hubo un problema al cargar los datos. Inténtalo de nuevo.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
fun TransactionCreateLoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            CircularProgressIndicator(
                color = ProgressIndicatorDefaults.circularColor,
                strokeWidth = 5.dp,
                trackColor = ProgressIndicatorDefaults.circularTrackColor,
                strokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "Obteniendo cuentas"
            )
        }
    )
}