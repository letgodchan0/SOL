package com.finance.android.viewmodels

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.finance.android.datastore.UserStore
import com.finance.android.domain.RetrofitClient
import com.finance.android.domain.dto.request.AccountNumberDto
import com.finance.android.domain.dto.request.CheckAccountRequestDto
import com.finance.android.domain.dto.request.RemitInfoRequestDto
import com.finance.android.domain.dto.request.RemitPhoneRequestDto
import com.finance.android.domain.dto.response.BankInfoResponseDto
import com.finance.android.domain.dto.response.RecentMyTradeResponseDto
import com.finance.android.domain.dto.response.RecentTradeResponseDto
import com.finance.android.domain.exception.NoMemberException
import com.finance.android.domain.repository.BankRepository
import com.finance.android.domain.repository.BaseRepository
import com.finance.android.domain.repository.RemitRepository
import com.finance.android.utils.Const
import com.finance.android.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RemitViewModel @Inject constructor(
    application: Application,
    baseRepository: BaseRepository,
    savedStateHandle: SavedStateHandle,
    private val remitRepository: RemitRepository,
    private val bankRepository: BankRepository
) : BaseViewModel(application, baseRepository) {
    val accountName = savedStateHandle.get<String>("accountName")!! // acName
    val accountNumber = savedStateHandle.get<String>("accountNumber")!! // ac_send
    val moneyValue = mutableStateOf("")
    val balance = savedStateHandle.get<Int>("balance")
    val enabledBackHeader = mutableStateOf(false)
    val requestRemit = mutableStateOf(false)
    var cpCode = mutableStateOf(0)
    val isBackToMain = mutableStateOf(false)

    private val _recommendedAccountData =
        mutableStateOf<Response<MutableList<RecentTradeResponseDto>>>(Response.Loading)
    val recommendedAccountData = _recommendedAccountData

    fun getRecommendedAccountData() {
        viewModelScope.launch {
            this@RemitViewModel.run {
                remitRepository.getRecommendedAccount()
            }
                .collect {
                    _recommendedAccountData.value = it
                }
            this@RemitViewModel.run {
                bankRepository.getRecentMyAccount()
            }.collect {
                _recentMyAccountData.value = it
            }

        }
    }

    // ?????? ??? ?????? ??????
    private val _recentMyAccountData =
        mutableStateOf<Response<MutableList<RecentMyTradeResponseDto>>>(Response.Loading)
    val recentMyAccountData = _recentMyAccountData

    fun getLoadRecommendation(): Response<Unit> {
        val arr = arrayOf(_recommendedAccountData, _recentMyAccountData)

        return if (arr.count { it.value is Response.Loading } != 0) {
            Response.Loading
        } else if (arr.count { it.value is Response.Failure } != 0) {
            Response.Failure(null)
        } else {
            Response.Success(Unit)
        }
    }

    //?????? ?????? ?????? ??????
    private val _allBankData =
        mutableStateOf<Response<MutableList<BankInfoResponseDto>>>(Response.Loading)
    val allBankData = _allBankData
    fun getAllBankData() {
        viewModelScope.launch {
            this@RemitViewModel.run {
                bankRepository.getAllBank()
            }
                .collect {
                    _allBankData.value = it
                }
        }
    }

    // ?????? ?????? ?????? ??????
    private val _allStockCpData =
        mutableStateOf<Response<MutableList<BankInfoResponseDto>>>(Response.Loading)
    val allStockCpData = _allStockCpData
    fun getAllStockCpData() {
        viewModelScope.launch {
            this@RemitViewModel.run {
                bankRepository.getAllStockCp()
            }
                .collect {
                    _allStockCpData.value = it
                }
        }
    }

    // ?????? ??????
    var isRightAccount = mutableStateOf(true)
    var validReceiveAccountNumber = mutableStateOf("0")
    private var validReceiveBankName = mutableStateOf("0")

    fun checkRightAccount(acNo: String, cpCode: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            this@RemitViewModel.run {
                bankRepository.checkAccount(CheckAccountRequestDto(acNo, cpCode))
            }.collect {
                if (it is Response.Success) {
                    if (it.data.userName.isEmpty()) {
                        isRightAccount.value = false
                    } else {
                        Log.i("test", "Success")
                        validReceiveAccountNumber.value = acNo
                        validReceiveBankName.value = selectedReceiveBank.value!!.cpName
                        onSuccess()
                    }
                }
            }
        }
    }

    // ??????????????? ????????????
    fun remitFromAccount(
        value: Int,
        receive: String,
        send: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            this@RemitViewModel.run {
                remitRepository.postRemitToAccount(
                    RemitInfoRequestDto(
                        acTag = selectedReceiveBank.value!!.cpName,
                        acReceive = validReceiveAccountNumber.value,
                        acSend = accountNumber,
                        acName = accountName,
                        value = value,
                        receive = receive,
                        send = send
                    )
                )
            }.collect {
                if (it is Response.Success) {
                    Log.i("remitAccount", "?????????")
                    onSuccess()
                } else if (it is Response.Failure) {
                    Log.i("remitAccount", "???????????? ??????")
                }
            }
        }
    }

    val phoneNum = mutableStateOf("")

    // ???????????? ????????????
    fun onClickContact(phone: String) {
        phoneNum.value = phone
    }

    // ??????????????? ????????????
    fun remitFromPhone(
        value: Int,
        receive: String,
        send: String,
        onSuccess: (content: String) -> Unit
    ) {
        viewModelScope.launch {
            this@RemitViewModel.run {
                remitRepository.postRemitToPhone(
                    RemitPhoneRequestDto(
                        acSend = accountNumber,
                        acName = accountName,
                        value = value,
                        receive = receive,
                        send = send,
                        phone = phoneNum.value.replace("-", "")
                    )
                )
            }.collect {
                if (it is Response.Success) {
                    Log.i("remitAccount", "????????? ?????????")
                    onSuccess("?????? ??????")
                } else if (it is Response.Failure) {
                    Log.i("remitAccount", "????????? ???????????? ??????")
                    if (it.e is HttpException && it.e.code() == 405) {
                        val converter = RetrofitClient.getInstance()
                            .responseBodyConverter<NoMemberException>(
                                NoMemberException::class.java,
                                NoMemberException::class.java.annotations
                            )
                        val token = converter.convert(it.e.response()!!.errorBody()!!)!!.tokenId
                        UserStore(getApplication()).getValue(UserStore.KEY_USER_NAME)
                            .collect { name ->
                                val link =
                                    "${Const.WEB_API}remit?query=${encodeValue("$token/${UUID.randomUUID()}")}"

                                val smsUri = Uri.parse("sms:${phoneNum.value.replace("-", "")}")
                                val sendIntent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    putExtra(
                                        "sms_body",
                                        "${name}?????? ${DecimalFormat("#,###???").format(value)}??? ????????????.\n?????? ????????? ???????????? ?????? ?????? ?????? ????????? ???????????????.\n$link"
                                    )
                                }
                                getApplication<Application>().startActivity(sendIntent)
                            }
                        onSuccess(
                            "?????? ???????????? ????????? ????????????\n" +
                                "?????? ?????? ?????? ??????, ????????? ???????????? ????????? ????????????"
                        )
                    }
                }
            }
        }
    }

    private fun encodeValue(value: String): String {
        return Base64.encodeToString(
            value.toByteArray(),
            Base64.DEFAULT
        )
    }

    // ?????????
    fun onClickAccountBookmark(recentTradeResponseDto: RecentTradeResponseDto) {
        viewModelScope.launch {
            this@RemitViewModel.run {
                remitRepository.putRemitBookmark(
                    accountNumberDto = AccountNumberDto(
                        recentTradeResponseDto.acNo
                    )
                )
            }.collect {
                if (it is Response.Success) {
                    Log.i("remitAccount", "????????? ?????????")
                    recentTradeResponseDto.bkStatus = !recentTradeResponseDto.bkStatus
                    recommendedAccountData.value =
                        Response.Success(
                            (recommendedAccountData.value as Response.Success).data.sortedWith { a, b ->
                                if (a.bkStatus && b.bkStatus) {
                                    return@sortedWith b.tdData.compareTo(a.tdData)
                                } else if (a.bkStatus) {
                                    return@sortedWith -1
                                } else {
                                    return@sortedWith 1
                                }
                            }
                                .toMutableList()
                        )
                } else if (it is Response.Failure) {
                    Log.i("remitAccount", "????????? ???????????? ??????")
                }
            }
        }
    }

    fun onClickAccountBookmark(recentMyTradeResponseDto: RecentMyTradeResponseDto) {
        viewModelScope.launch {
            this@RemitViewModel.run {
                remitRepository.putRemitBookmark(
                    accountNumberDto = AccountNumberDto(
                        recentMyTradeResponseDto.acNo
                    )
                )
            }.collect {
                if (it is Response.Success) {
                    Log.i("remitAccount", "????????? ?????????")
                    recentMyTradeResponseDto.bkStatus = !recentMyTradeResponseDto.bkStatus
                    recentMyAccountData.value =
                        Response.Success(
                            mutableListOf<RecentMyTradeResponseDto>().apply {
                                addAll(
                                    (recentMyAccountData.value as Response.Success).data.sortedWith { a, b ->
                                        if (a.bkStatus && b.bkStatus) {
                                            return@sortedWith a.acNo.compareTo(b.acNo)
                                        } else if (a.bkStatus) {
                                            return@sortedWith -1
                                        } else {
                                            return@sortedWith 1
                                        }
                                    }
                                )
                            }
                        )
                } else if (it is Response.Failure) {
                    Log.i("remitAccount", "????????? ???????????? ??????")
                }
            }
        }
    }

    fun onClickAccount(key: Any) {
        _recommendedAccountData.value
    }

    val selectedReceiveBank = mutableStateOf<BankInfoResponseDto?>(null)
    fun onClickReceiveBank(bankInfoResponseDto: BankInfoResponseDto) {
        selectedReceiveBank.value = bankInfoResponseDto
    }
}
