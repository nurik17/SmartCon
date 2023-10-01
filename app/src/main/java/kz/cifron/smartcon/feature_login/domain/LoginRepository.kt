package kz.cifron.smartcon.feature_login.domain

import kz.cifron.smartcon.feature_login.data.LoginRequestBody
import kz.cifron.smartcon.feature_login.data.LoginResponse
import retrofit2.Response

class LoginRepository(private val apiService: LoginApi) {

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val loginRequest = LoginRequestBody(email, password)
        return apiService.login(loginRequest)
    }
}