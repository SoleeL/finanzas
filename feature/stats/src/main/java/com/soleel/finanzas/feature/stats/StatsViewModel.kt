package com.soleel.finanzas.feature.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soleel.finanzas.core.common.result.Result
import com.soleel.finanzas.core.common.result.asResult
import com.soleel.finanzas.core.common.retryflow.RetryableFlowTrigger
import com.soleel.finanzas.core.common.retryflow.retryableFlow
import com.soleel.finanzas.core.model.Stat
import com.soleel.finanzas.domain.stats.GetCategoryStatsUseCase
import com.soleel.finanzas.domain.stats.GetGeneralStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


sealed interface StatsUiState {
    data class Success(
        val generalStats: List<Stat>,
        val categoryStats: Pair<List<Stat>, List<Stat>>
    ) : StatsUiState

    data object Error : StatsUiState
    data object Loading : StatsUiState
}

sealed class StatsUiEvent {
    data object Retry : StatsUiEvent()
}

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getGeneralStatsUseCase: GetGeneralStatsUseCase,
    private val getCategoryStatsUseCase: GetCategoryStatsUseCase,

    private val retryableFlowTrigger: RetryableFlowTrigger
) : ViewModel() {

    private val _statsUiState: Flow<StatsUiState> = retryableFlowTrigger
        .retryableFlow(flowProvider = { getFlowStats() })

    private fun getFlowStats(): Flow<StatsUiState> {
        return combine(
            flow = getGeneralStatsUseCase().asResult(),
            flow2 = getCategoryStatsUseCase().asResult(),
            transform = { generalStatResult, categoryStatResult ->
                getStatUiState(generalStatResult, categoryStatResult)
            }
        )
    }

    private fun getStatUiState(
        generalStatResult: Result<List<Stat>>,
        categoryStatResult: Result<Pair<List<Stat>, List<Stat>>>
    ): StatsUiState {
        return when {
            generalStatResult is Result.Loading || categoryStatResult is Result.Loading -> {
                StatsUiState.Loading
            }

            generalStatResult is Result.Success && categoryStatResult is Result.Success -> {
                StatsUiState.Success(
                    generalStats = generalStatResult.data,
                    categoryStats = categoryStatResult.data
                )
            }

            else -> StatsUiState.Error
        }
    }

    val statsUiState: StateFlow<StatsUiState> = _statsUiState
        .onStart(action = { delay(500) })
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = StatsUiState.Loading
        )

    fun onStatsUiEvent(event: StatsUiEvent) {
        when (event) {
            is StatsUiEvent.Retry -> {
                retryableFlowTrigger.retry()
            }
        }
    }
}