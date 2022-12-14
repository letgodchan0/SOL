package com.finance.android.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.finance.android.domain.dto.response.*
import com.finance.android.domain.repository.BaseRepository
import com.finance.android.domain.repository.InsuranceRepository
import com.finance.android.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsuranceViewModel @Inject constructor(
    application: Application,
    baseRepository: BaseRepository,
    private val insuranceRepository: InsuranceRepository,
) : BaseViewModel(application, baseRepository) {
    val isList =
        mutableStateOf<MyInsuranceInfoResponseDto?>(null)

    fun myIsLoad() {
        viewModelScope.launch {
            loadMyIsList()
        }
    }

    private suspend fun loadMyIsList() {
        this@InsuranceViewModel.run {
            insuranceRepository.getMyInsurance()
        }
            .collect {
                if (it is Response.Success) {
                    isList.value = it.data
                }
            }
    }

}