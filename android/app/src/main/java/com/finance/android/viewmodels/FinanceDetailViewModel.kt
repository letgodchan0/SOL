package com.finance.android.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.finance.android.domain.dto.response.FinanceDetailResponseDto
import com.finance.android.domain.repository.BaseRepository
import com.finance.android.domain.repository.StockRepository
import com.finance.android.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinanceDetailViewModel @Inject constructor(
    application: Application,
    baseRepository: BaseRepository,
    savedStateHandle: SavedStateHandle,
    private val stockRepository: StockRepository
) : BaseViewModel(application, baseRepository) {
    val fnName = savedStateHandle.get<String>("fnName")!!
    val close = savedStateHandle.get<Int>("close")!!
    val per = savedStateHandle.get<Float>("per")!!

    val financeDetailList = mutableStateListOf<FinanceDetailResponseDto>()

    fun launch() {
        viewModelScope.launch {
            loadFinanceDetailList(fnName)
        }
    }

    private suspend fun loadFinanceDetailList(fnName: String) {
        this@FinanceDetailViewModel.run {
            stockRepository.getFinanceDetailList(fnName)
        }
            .collect {
                if (it is Response.Success) {
                    financeDetailList.clear()
                    financeDetailList.addAll(it.data)
                }
            }
    }
}
