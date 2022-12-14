package com.finance.android.domain.dto.response

import com.google.gson.annotations.SerializedName

data class BankAccountResponseDto(

    @SerializedName("ac_no")
    val acNo: String, //계좌번호
    @SerializedName("balance")
    val balance: Int, //잔액
    @SerializedName("ac_name")
    val acName: String, //계좌 이름
    @SerializedName("cp_name")
    val cpName: String, //기업명
    @SerializedName("cp_logo")
    val cpLogo: String, //기업 로고 경로
    @SerializedName("ac_reg")
    val isRegister: Boolean, // 등록 여부
    @SerializedName("ac_main")
    val acMain: Int, // 대표계좌 여부
    @SerializedName("ac_type")
    val acType: Int, // 계좌 타입 (1 : 예금, 2 : 증권)

)