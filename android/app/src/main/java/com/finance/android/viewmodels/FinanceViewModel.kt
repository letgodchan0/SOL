package com.finance.android.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.finance.android.domain.dto.response.BankAccountResponseDto
import com.finance.android.domain.dto.response.FinanceResponseDto
import com.finance.android.domain.repository.BaseRepository
import com.finance.android.domain.repository.StockRepository
import com.finance.android.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FinanceViewModel @Inject constructor(
    application: Application,
    baseRepository: BaseRepository,
    private val stockRepository: StockRepository
) : BaseViewModel(application, baseRepository){
    val financeList = mutableStateOf<Response<Array<FinanceResponseDto>>>(Response.Loading)
    val myFinanceList = mutableStateOf<Response<MutableList<BankAccountResponseDto>>>(Response.Loading)

    fun Load() {
       viewModelScope.launch {
           loadFinanceList()
       }
    }

    fun myFinanceLoad() {
        viewModelScope.launch {
            loadMyFinanceList()
        }
    }

    fun getLoadState(): Response<Unit> {
        val arr = arrayOf(financeList)

        return if (arr.count { it.value is Response.Loading } != 0) {
            Response.Loading
        } else if (arr.count { it.value is Response.Failure } != 0) {
            Response.Failure(null)
        } else {
            Response.Success(Unit)
        }
    }

    fun getLoadState2(): Response<Unit> {
        val arr = arrayOf(myFinanceList)

        return if (arr.count { it.value is Response.Loading } != 0) {
            Response.Loading
        } else if (arr.count { it.value is Response.Failure } != 0) {
            Response.Failure(null)
        } else {
            Response.Success(Unit)
        }
    }

    private suspend fun loadFinanceList() {
        this@FinanceViewModel.run {
            stockRepository.getFinanceList()
        }
            .collect {
                financeList.value = it
            }
    }

    private suspend fun loadMyFinanceList() {
        this@FinanceViewModel.run {
            stockRepository.getMyFinanceList()
        }
            .collect {
                myFinanceList.value = it
            }
    }

}