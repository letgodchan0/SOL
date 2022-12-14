package com.finance.android.domain.service

import com.finance.android.domain.dto.request.*
import com.finance.android.domain.dto.response.LoginResponseDto
import com.finance.android.domain.dto.response.PushTokenRequestDto
import com.finance.android.domain.dto.response.UserProfileResponseDto
import com.finance.android.utils.Const
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {

    @POST("${Const.API_PATH}/auth/signup/check")
    suspend fun checkUser(@Body checkUserRequestDto: CheckUserRequestDto)

    @POST("${Const.API_PATH}/auth/login")
    suspend fun login(@Body loginRequestDto: LoginRequestDto): LoginResponseDto

    @POST("${Const.API_PATH}/auth/relogin")
    suspend fun reLogin(@Body reLoginRequestDto: ReLoginRequestDto): LoginResponseDto

    @POST("${Const.API_PATH}/auth/signup")
    suspend fun signup(@Body signupRequestDto: SignupRequestDto): LoginResponseDto

    @POST("${Const.DATA_PATH}/user/register")
    suspend fun createAsset(@Body createAssetRequestDto: CreateAssetRequestDto)

    @GET("${Const.API_PATH}/user/account")
    suspend fun checkRepAccount(): Boolean

    @PUT("${Const.API_PATH}/user/account")
    suspend fun changeRepAccount(@Body mainAccountDto: MainAccountDto)

    @GET("${Const.API_PATH}/user")
    suspend fun getUserProfile() : UserProfileResponseDto

    @POST("${Const.API_PATH}/point/easter")
    suspend fun receivePoint(@Body receivePointDto: ReceivePointDto)

    @PUT("${Const.API_PATH}/notice")
    suspend fun putPushToken(@Body pushTokenRequestDto: PushTokenRequestDto)
}
