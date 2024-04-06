package com.soleel.transactioncreate.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.soleel.transactioncreate.TransactionCreateRoute
import com.soleel.transactioncreate.TransactionCreateViewModel
import com.soleel.transactioncreate.screen.TransactionAmountRoute
import com.soleel.transactioncreate.screen.TransactionCategoryRoute
import com.soleel.transactioncreate.screen.TransactionNameRoute
import com.soleel.transactioncreate.screen.TransactionPaymentAccountRoute
import com.soleel.transactioncreate.screen.TransactionTypeRoute


const val transactionCreateGraph = "transaction_create_graph"

const val transactionCreateRoute = "transaction_create_route"
const val transactionPaymentAccountRoute = "transaction_payment_account_route"
const val transactionCategoryRoute = "transaction_category_route"
const val transactionTypeRoute = "transaction_type_route"
const val transactionNameRoute = "transaction_name_route"
const val transactionAmountRoute = "transaction_amount_route"

fun NavController.navigateToTransactionCreateGraph(navOptions: NavOptions? = null) {
    this.navigate(transactionCreateGraph, navOptions)
}

fun NavController.navigateToTransactionPaymentAccountRoute(navOptions: NavOptions? = null) {
    this.navigate(transactionPaymentAccountRoute, navOptions)
}

fun NavController.navigateToTransactionTypeRoute(navOptions: NavOptions? = null) {
    this.navigate(transactionTypeRoute, navOptions)
}

fun NavController.navigateToTransactionCategoryRoute(navOptions: NavOptions? = null) {
    this.navigate(transactionCategoryRoute, navOptions)
}

fun NavController.navigateToTransactionNameRoute(navOptions: NavOptions? = null) {
    this.navigate(transactionNameRoute, navOptions)
}

fun NavController.navigateToTransactionAmountRoute(navOptions: NavOptions? = null) {
    this.navigate(transactionAmountRoute, navOptions)
}

fun NavGraphBuilder.transactionCreateGraph(
    navController: NavHostController,
    onShowBottomBar: () -> Unit,
    onShowAddFloating: () -> Unit,
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    fromInitToPaymentAccount: () -> Unit,
    fromPaymentAccountToType: () -> Unit,
    fromTypeToCategory: () -> Unit,
    fromCategoryToName: () -> Unit,
    fromNameToAmount: () -> Unit
) {
    navigation(
        startDestination = transactionCreateRoute,
        route = transactionCreateGraph,
        builder = {
            transactionCreateRoute(
                navController = navController,
                onShowBottomBar = onShowBottomBar,
                onShowAddFloating = onShowAddFloating,
                onBackClick = onBackClick,
                fromInitToPaymentAccount = fromInitToPaymentAccount
            )
            transactionPaymentAccountRoute(
                navController = navController,
                onCancelClick = onCancelClick,
                fromPaymentAccountToType = fromPaymentAccountToType
            )
            transactionTypeRoute(
                navController = navController,
                onCancelClick = onCancelClick,
                onBackClick = onBackClick,
                fromTypeToCategory = fromTypeToCategory
            )
            transactionCategoryRoute(
                navController = navController,
                onCancelClick = onCancelClick,
                onBackClick = onBackClick,
                fromCategoryToName = fromCategoryToName
            )
            transactionNameRoute(
                navController = navController,
                onCancelClick = onCancelClick,
                onBackClick = onBackClick,
                fromNameToAmount = fromNameToAmount
            )
            transactionAmountRoute(
                navController = navController,
                onShowBottomBar = onShowBottomBar,
                onShowAddFloating = onShowAddFloating,
                onCancelClick = onCancelClick,
                onBackClick = onBackClick,
                onSaveClick = onSaveClick
            )
        }
    )
}

fun NavGraphBuilder.transactionCreateRoute(
    navController: NavHostController,
    onShowBottomBar: () -> Unit,
    onShowAddFloating: () -> Unit,
    onBackClick: () -> Unit,
    fromInitToPaymentAccount: () -> Unit
) {
    composable(
        route = transactionCreateRoute,
        content = {

            val parentEntry = remember(
                key1 = it,
                calculation = {
                    navController.getBackStackEntry(route = transactionCreateGraph)
                }
            )

            val viewModel: TransactionCreateViewModel = hiltViewModel(
                viewModelStoreOwner = parentEntry
            )

            TransactionCreateRoute(
                onShowBottomBar = onShowBottomBar,
                onShowAddFloating = onShowAddFloating,
                onBackClick = onBackClick,
                fromInitToPaymentAccount = fromInitToPaymentAccount,
                viewModel = viewModel
            )
        }
    )
}

fun NavGraphBuilder.transactionPaymentAccountRoute(
    navController: NavHostController,
    onCancelClick: () -> Unit,
    fromPaymentAccountToType: () -> Unit
) {
    composable(
        route = transactionPaymentAccountRoute,
        content = {

            val parentEntry = remember(
                key1 = it,
                calculation = {
                    navController.getBackStackEntry(route = transactionCreateGraph)
                }
            )

            val viewModel: TransactionCreateViewModel = hiltViewModel(
                viewModelStoreOwner = parentEntry
            )

            TransactionPaymentAccountRoute(
                onCancelClick = onCancelClick,
                fromPaymentAccountToType = fromPaymentAccountToType,
                viewModel = viewModel
            )
        }
    )
}

fun NavGraphBuilder.transactionTypeRoute(
    navController: NavHostController,
    onCancelClick: () -> Unit,
    onBackClick: () -> Unit,
    fromTypeToCategory: () -> Unit,
) {
    composable(
        route = transactionTypeRoute,
        content = {

            val parentEntry = remember(
                key1 = it,
                calculation = {
                    navController.getBackStackEntry(route = transactionCreateGraph)
                }
            )

            val viewModel: TransactionCreateViewModel = hiltViewModel(
                viewModelStoreOwner = parentEntry
            )

            TransactionTypeRoute(
                onCancelClick = onCancelClick,
                onBackClick = onBackClick,
                fromTypeToCategory = fromTypeToCategory,
                viewModel = viewModel
            )
        }
    )
}

fun NavGraphBuilder.transactionCategoryRoute(
    navController: NavHostController,
    onCancelClick: () -> Unit,
    onBackClick: () -> Unit,
    fromCategoryToName: () -> Unit,

    ) {
    composable(
        route = transactionCategoryRoute,
        content = {

            val parentEntry = remember(
                key1 = it,
                calculation = {
                    navController.getBackStackEntry(route = transactionCreateGraph)
                }
            )

            val viewModel: TransactionCreateViewModel = hiltViewModel(
                viewModelStoreOwner = parentEntry
            )

            TransactionCategoryRoute(
                onCancelClick = onCancelClick,
                onBackClick = onBackClick,
                fromCategoryToName = fromCategoryToName,
                viewModel = viewModel
            )
        }
    )
}

fun NavGraphBuilder.transactionNameRoute(
    navController: NavHostController,
    onCancelClick: () -> Unit,
    onBackClick: () -> Unit,
    fromNameToAmount: () -> Unit,
) {
    composable(
        route = transactionNameRoute,
        content = {

            val parentEntry = remember(
                key1 = it,
                calculation = {
                    navController.getBackStackEntry(route = transactionCreateGraph)
                }
            )

            val viewModel: TransactionCreateViewModel = hiltViewModel(
                viewModelStoreOwner = parentEntry
            )

            TransactionNameRoute(
                onCancelClick = onCancelClick,
                onBackClick = onBackClick,
                fromNameToAmount = fromNameToAmount,
                viewModel = viewModel
            )
        }
    )
}

fun NavGraphBuilder.transactionAmountRoute(
    navController: NavHostController,
    onShowBottomBar: () -> Unit,
    onShowAddFloating: () -> Unit,
    onCancelClick: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    composable(
        route = transactionAmountRoute,
        content = {

            val parentEntry = remember(
                key1 = it,
                calculation = {
                    navController.getBackStackEntry(route = transactionCreateGraph)
                }
            )

            val viewModel: TransactionCreateViewModel = hiltViewModel(
                viewModelStoreOwner = parentEntry
            )

            TransactionAmountRoute(
                onShowBottomBar = onShowBottomBar,
                onShowAddFloating = onShowAddFloating,
                onCancelClick = onCancelClick,
                onBackClick = onBackClick,
                onSaveClick = onSaveClick,
                viewModel = viewModel
            )
        }
    )
}