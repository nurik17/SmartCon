package kz.cifron.smartcon.feature_login.domain

import kz.cifron.smartcon.feature_login.data.LoginRequestBody
import kz.cifron.smartcon.feature_login.data.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface LoginApi {
    @POST("login")
    suspend fun login(@Body request : LoginRequestBody) : Response<LoginResponse>
}