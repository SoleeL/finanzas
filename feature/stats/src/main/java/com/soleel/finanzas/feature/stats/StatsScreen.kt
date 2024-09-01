package com.soleel.finanzas.feature.stats

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.soleel.finanzas.core.model.Stat
import com.soleel.finanzas.core.model.enums.TransactionCategoryEnum
import com.soleel.finanzas.core.model.enums.TransactionTypeEnum
import com.soleel.finanzas.core.ui.R
import com.soleel.finanzas.domain.transformation.visualtransformation.CurrencyVisualTransformation
import java.time.LocalDate


@Composable
internal fun StatsRoute(
    modifier: Modifier = Modifier,
    finishApp: (Context) -> Unit,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val statsUiState: StatsUiState by viewModel.statsUiState.collectAsStateWithLifecycle()

    StatsScreen(
        modifier = modifier,
        finishApp = finishApp,
        statsUiState = statsUiState,
        onStatsUiEvent = viewModel::onStatsUiEvent
    )
}

@Composable
fun StatsScreen(
    modifier: Modifier,
    finishApp: (Context) -> Unit,
    statsUiState: StatsUiState,
    onStatsUiEvent: (StatsUiEvent) -> Unit
) {
    val context: Context = LocalContext.current

    BackHandler(
        enabled = true,
        onBack = {
            Log.d("finanzas", "AllTransactionsListScreen: back press")
            finishApp(context)
        }
    )

    when (statsUiState) {
        is StatsUiState.Success -> StatsSuccessScreen(
            generalStats = statsUiState.generalStats,
            categoryStats = statsUiState.categoryStats
        )

        is StatsUiState.Error -> StatsErrorScreen(
            modifier = modifier,
            onRetry = { onStatsUiEvent(StatsUiEvent.Retry) }
        )

        is StatsUiState.Loading -> StatsLoadingScreen()
    }
}

@Preview
@Composable
fun StatsSuccessScreenPreview() {
    StatsSuccessScreen(
        generalStats = listOf<Stat>(
            Stat(
                type = TransactionTypeEnum.EXPENDITURE,
                amount = 50000,
                transactionNumber = 3
            ),
            Stat(
                amount = 50000
            ),
            Stat(
                type = TransactionTypeEnum.INCOME,
                amount = 100000,
                transactionNumber = 7
            )
        ),
        categoryStats = Pair<List<Stat>, List<Stat>>(
            first = listOf(
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_GIFT,
                    amount = 100000,
                    transactionNumber = 7
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_MARKET,
                    amount = 50000,
                    transactionNumber = 3
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                )
            ),
            second = listOf(
                Stat(
                    category = TransactionCategoryEnum.INCOME_BONUS,
                    amount = 100000,
                    transactionNumber = 7
                ),
                Stat(
                    category = TransactionCategoryEnum.INCOME_SALARY,
                    amount = 1000000,
                    transactionNumber = 8
                ),
                Stat(
                    category = TransactionCategoryEnum.INCOME_SERVICE,
                    amount = 200000,
                    transactionNumber = 16
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                ),
                Stat(
                    category = TransactionCategoryEnum.EXPENDITURE_ACQUISITION,
                    amount = 200000,
                    transactionNumber = 90
                )
            )
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatsSuccessScreen(
    generalStats: List<Stat>,
    categoryStats: Pair<List<Stat>, List<Stat>>
) {
    val currencyVisualTransformation by remember(calculation = {
        mutableStateOf(CurrencyVisualTransformation(currencyCode = "USD"))
    })

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 72.dp),
        content = {
            item(
                content = {
                    Text(
                        text = "Estadisticas generales",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 16.dp,
                            end = 8.dp,
                            bottom = 8.dp
                        )
                    )
                }
            )

            items(
                items = generalStats,
                itemContent = { generalStat: Stat ->
                    GeneralStat(
                        type = generalStat.type,
                        amount = currencyVisualTransformation.filter(
                            AnnotatedString(
                                text = generalStat.amount.toString()
                            )
                        ).text.toString(),
                        transactionsNumber = generalStat.transactionNumber
                    )
                }
            )

//            item(
//                content = {
//                    Text(
//                        text = "Estadisticas por categorias",
//                        style = MaterialTheme.typography.titleLarge,
//                        modifier = Modifier.padding(
//                            start = 8.dp,
//                            top = 16.dp,
//                            end = 8.dp
////                            bottom = 8.dp
//                        )
//                    )
//                }
//            )


            stickyHeader(
                content = {
                    Text(
                        text = "Estadisticas por categorias",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 16.dp,
                            end = 8.dp
//                            bottom = 8.dp
                        )
                    )

                    CategoryStatsGroupTitle(title = "Categorias de gastos")
                }
            )

            items(
                items = categoryStats.first,
                itemContent = { categoryStat: Stat ->
                    CategoryStat(
                        categoryIcon = categoryStat.category?.icon ?: R.drawable.ic_balance,
                        categoryName = categoryStat.category?.value ?: "",
                        amount = currencyVisualTransformation.filter(
                            AnnotatedString(
                                text = categoryStat.amount.toString()
                            )
                        ).text.toString(),
                        transactionsNumber = categoryStat.transactionNumber,
                    )
                }
            )

            stickyHeader(
                content = {
                    CategoryStatsGroupTitle(title = "Categorias de ingresos")
                }
            )

            items(
                items = categoryStats.second,
                itemContent = { categoryStat: Stat ->
                    CategoryStat(
                        categoryIcon = categoryStat.category?.icon ?: R.drawable.ic_balance,
                        categoryName = categoryStat.category?.value ?: "",
                        amount = currencyVisualTransformation.filter(
                            AnnotatedString(
                                text = categoryStat.amount.toString()
                            )
                        ).text.toString(),
                        transactionsNumber = categoryStat.transactionNumber,
                    )
                }
            )

        }
    )
}

@Composable
private fun CategoryStatsGroupTitle(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.Start,
        content = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                )
            )
        }
    )
}

@Composable
fun GeneralStat(
    type: TransactionTypeEnum?,
    amount: String,
    transactionsNumber: Int? = null
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            Icon(
                                painter = painterResource(id = type?.icon ?: R.drawable.ic_balance),
                                contentDescription = "Account type",
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = type?.value ?: "Balance",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            Text(
                                text = amount,
                                style = MaterialTheme.typography.displaySmall
                            )

                            if (null != transactionsNumber) {
                                Text(
                                    text = "$transactionsNumber transacciones",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text = "Monto disponible",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun CategoryStat(
    categoryIcon: Int,
    categoryName: String,
    amount: String,
    transactionsNumber: Int? = null
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Column(
                        content = {
                            Icon(
                                painter = painterResource(id = categoryIcon),
                                contentDescription = "Account type",
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    )

                    Column(
                        content = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                content = {
                                    Text(
                                        text = categoryName,
                                        style = MaterialTheme.typography.titleLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = amount,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                content = {
                                    Text(
                                        text = "$transactionsNumber transacciones",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun StatsErrorScreen(
    modifier: Modifier,
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
fun StatsLoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = {
            CircularProgressIndicator(
                color = ProgressIndicatorDefaults.circularColor,
                strokeWidth = 5.dp,
                trackColor = ProgressIndicatorDefaults.circularTrackColor,
                strokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap
            )
        }
    )
}
