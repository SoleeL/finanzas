package com.soleel.finanzas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.soleel.finanzas.feature.accountcreate.navigation.accountCreateGraph
import com.soleel.finanzas.feature.accountcreate.navigation.navigateToAccountAmountRoute
import com.soleel.finanzas.feature.accountcreate.navigation.navigateToAccountNameRoute
import com.soleel.finanzas.feature.accounts.navigation.accountsScreen
import com.soleel.finanzas.feature.profile.navigation.profileScreen
import com.soleel.finanzas.feature.stats.navigation.statsScreen
import com.soleel.finanzas.feature.transactioncreate.navigation.navigateToTransactionAccountRoute
import com.soleel.finanzas.feature.transactioncreate.navigation.navigateToTransactionAmountRoute
import com.soleel.finanzas.feature.transactioncreate.navigation.navigateToTransactionCategoryRoute
import com.soleel.finanzas.feature.transactioncreate.navigation.navigateToTransactionNameRoute
import com.soleel.finanzas.feature.transactioncreate.navigation.navigateToTransactionTypeRoute
import com.soleel.finanzas.feature.transactioncreate.navigation.transactionCreateGraph
import com.soleel.finanzas.feature.transactions.navigation.SUMMARY_PERIOD_ARG
import com.soleel.finanzas.feature.transactions.navigation.TRANSACTIONS_ROUTE
import com.soleel.finanzas.feature.transactions.navigation.destination.TransactionsLevelDestination
import com.soleel.finanzas.feature.transactions.navigation.destination.TransactionsLevelDestination.Companion.lowercase
import com.soleel.finanzas.feature.transactions.navigation.transactionsScreen
import com.soleel.finanzas.ui.FinanzasAppState


@Composable
fun FinanzasNavHost(
    appState: FinanzasAppState,
    modifier: Modifier = Modifier,
    startDestination: String = "$TRANSACTIONS_ROUTE/{$SUMMARY_PERIOD_ARG}",
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        builder = {
            transactionsScreen(
                finishApp = appState::finishApp
            )

            statsScreen(
                finishApp = appState::finishApp
            )

            accountsScreen(
                finishApp = appState::finishApp
            )

            profileScreen(
                finishApp = appState::finishApp
            )

            accountCreateGraph(
                navController = navController,
                showTransactionsTab = appState::showTransactionsTab,
                showBottomBar = appState::showBottomBar,
                showFloatingAddMenu = appState::showFloatingAddMenu,
                hideExtendAddMenu = appState::hideExtendAddMenu,
                onBackClick = navController::popBackStack,
                onCancelClick = appState::showCancelAlert,
                onSaveClick = appState::backToHome,
                fromTypeToName = navController::navigateToAccountNameRoute,
                fromNameToAmount = navController::navigateToAccountAmountRoute
            )

            transactionCreateGraph(
                navController = navController,
                showTransactionsTab = appState::showTransactionsTab,
                showBottomBar = appState::showBottomBar,
                showFloatingAddMenu = appState::showFloatingAddMenu,
                hideExtendAddMenu = appState::hideExtendAddMenu,
                onBackClick = navController::popBackStack,
                onCancelClick = appState::showCancelAlert,
                onSaveClick = appState::backToHome,
                fromInitToAccount = navController::navigateToTransactionAccountRoute,
                fromAccountToType = navController::navigateToTransactionTypeRoute,
                fromTypeToCategory = navController::navigateToTransactionCategoryRoute,
                fromCategoryToName = navController::navigateToTransactionNameRoute,
                fromNameToAmount = navController::navigateToTransactionAmountRoute,
            )
        }
    )
}

//fun NavGraphBuilder.finanzas(navController: NavHostController): NavGraphBuilder {
//    homeScreen()
//    ...
//}