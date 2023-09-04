package kz.cifron.smartcon.presentation.login

import retrofit2.Response

class LoginRepository(private val apiService: LoginApi) {

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val loginRequest = LoginRequestBody(email, password)
        return apiService.login(loginRequest)
    }
}