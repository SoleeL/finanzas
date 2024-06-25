package com.soleel.finanzas.feature.transactioncreate.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soleel.finanzas.core.common.enums.AccountTypeEnum
import com.soleel.finanzas.core.common.enums.TransactionCategoryEnum
import com.soleel.finanzas.core.common.enums.TransactionTypeEnum
import com.soleel.finanzas.core.ui.R
import com.soleel.finanzas.core.ui.template.TransactionCard
import com.soleel.finanzas.core.ui.template.TransactionCreateTopAppBar
import com.soleel.finanzas.core.ui.uivalues.getTransactionUI
import com.soleel.finanzas.core.model.Account
import com.soleel.finanzas.domain.transformation.visualtransformation.CurrencyVisualTransformation
import com.soleel.finanzas.domain.validation.validator.TransactionAmountValidator
import com.soleel.finanzas.feature.transactioncreate.TransactionCreateViewModel
import com.soleel.finanzas.feature.transactioncreate.TransactionUiCreate
import com.soleel.finanzas.feature.transactioncreate.TransactionUiEvent
import java.util.Date


@Composable
internal fun TransactionAmountRoute(
    showTransactionsTab: () -> Unit,
    showBottomBar: () -> Unit,
    showFloatingAddMenu: () -> Unit,
    hideExtendAddMenu: () -> Unit,
    onCancelClick: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: TransactionCreateViewModel
) {
    val transactionUiCreate: TransactionUiCreate = viewModel.transactionUiCreate

    TransactionAmountScreen(
        modifier = Modifier,
        showTransactionsTab = showTransactionsTab,
        showBottomBar = showBottomBar,
        showFloatingAddMenu = showFloatingAddMenu,
        hideExtendAddMenu = hideExtendAddMenu,
        onCancelClick = onCancelClick,
        onBackClick = onBackClick,
        onSaveClick = onSaveClick,
        transactionUiCreate = transactionUiCreate,
        onTransactionCreateUiEvent = viewModel::onTransactionCreateUiEvent
    )
}

@Preview
@Composable
fun TransactionAmountScreenPreview() {
    TransactionAmountScreen(
        modifier = Modifier,
        showTransactionsTab = {},
        showBottomBar = {},
        showFloatingAddMenu = {},
        hideExtendAddMenu = {},
        onCancelClick = {},
        onBackClick = {},
        onSaveClick = {},
        transactionUiCreate = TransactionUiCreate(
            account = Account(
                id = "2",
                name = "Cuenta corriente falabella",
                amount = 400000,
                createAt = Date(),
                updatedAt = Date(),
                type = AccountTypeEnum.CREDIT
            ),
            transactionType = TransactionTypeEnum.EXPENDITURE.id,
            transactionCategory = TransactionCategoryEnum.EXPENDITURE_GIFT.id,
            transactionName = "Regalo para abuela"
        ),
        onTransactionCreateUiEvent = {}
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TransactionAmountScreen(
    modifier: Modifier,
    showTransactionsTab: () -> Unit,
    showBottomBar: () -> Unit,
    showFloatingAddMenu: () -> Unit,
    hideExtendAddMenu: () -> Unit,
    onCancelClick: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    transactionUiCreate: TransactionUiCreate,
    onTransactionCreateUiEvent: (TransactionUiEvent) -> Unit
) {
    BackHandler(
        enabled = true,
        onBack = { onBackClick() }
    )

    if (transactionUiCreate.isTransactionSaved) {
        showTransactionsTab()
        showBottomBar()
        showFloatingAddMenu()
        hideExtendAddMenu()
        onSaveClick()
    }

    Scaffold(
        topBar = {
            TransactionCreateTopAppBar(
                subTitle = R.string.trasaction_amount_top_app_bar_subtitle,
                onClick = onCancelClick
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Button(
                        onClick = { onTransactionCreateUiEvent(TransactionUiEvent.Submit) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(64.dp),
                        enabled = 0 != transactionUiCreate.transactionAmount &&
                                null == transactionUiCreate.transactionAmountError,
                        content = { Text(text = "Guardar transaccion") }
                    )
                }
            )
        },
        content = {
            val currencyVisualTransformation by remember(calculation = {
                mutableStateOf(CurrencyVisualTransformation(currencyCode = "USD"))
            })

            val AccountAmount: String = currencyVisualTransformation
                .filter(AnnotatedString(text = transactionUiCreate.account.amount.toString()))
                .text
                .toString()

            val transactionAmount: String = currencyVisualTransformation
                .filter(AnnotatedString(text = transactionUiCreate.transactionAmount.toString()))
                .text
                .toString()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding()),
                content = {
                    TransactionCard(
                        transactionUIValues = getTransactionUI(
                            accountTypeEnum = AccountTypeEnum.fromId(transactionUiCreate.account.type.id),
                            accountName = transactionUiCreate.account.name,
                            accountAmount = AccountAmount,
                            transactionType = TransactionTypeEnum.fromId(transactionUiCreate.transactionType),
                            transactionCategory = TransactionCategoryEnum.fromId(transactionUiCreate.transactionCategory),
                            transactionName = transactionUiCreate.transactionName,
                            transactionAmount = transactionAmount)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    EnterTransactionAmountTextFlied(
                        transactionUiCreate = transactionUiCreate,
                        onTransactionCreateUiEvent = onTransactionCreateUiEvent,
                        currencyVisualTransformation = currencyVisualTransformation
                    )
                }
            )
        }
    )

}

@Composable
fun EnterTransactionAmountTextFlied(
    transactionUiCreate: TransactionUiCreate,
    onTransactionCreateUiEvent: (TransactionUiEvent) -> Unit,
    currencyVisualTransformation: CurrencyVisualTransformation,
) {
    OutlinedTextField(
        value = if (0 != transactionUiCreate.transactionAmount) transactionUiCreate.transactionAmount.toString() else "",
        onValueChange = { input: String ->
            val trimmed = input
                .trimStart('0')
                .trim(predicate = { inputTrimStart -> inputTrimStart.isDigit().not() })

            if (trimmed.length <= TransactionAmountValidator.maxCharLimit) {
                onTransactionCreateUiEvent(
                    TransactionUiEvent.TransactionAmountChanged(
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
        enabled = 0 != transactionUiCreate.transactionCategory,
        label = { Text(text = stringResource(id = R.string.attribute_trasaction_amount_field)) },
        trailingIcon = {
            if (null != transactionUiCreate.transactionAmountError) {
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
                text = if (null == transactionUiCreate.transactionAmountError)
                    stringResource(id = R.string.required_field) else
                    stringResource(id = transactionUiCreate.transactionAmountError),
                textAlign = TextAlign.End,
            )
        },
        isError = transactionUiCreate.transactionAmountError != null,
        visualTransformation = currencyVisualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}


