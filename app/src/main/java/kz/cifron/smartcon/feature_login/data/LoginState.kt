package kz.cifron.smartcon.feature_login.data


sealed class LoginState {
        data class Success(val user: User, val token: String) : LoginState()
        data class Error(val message: String) : LoginState()
        object Loading : LoginState()
}
