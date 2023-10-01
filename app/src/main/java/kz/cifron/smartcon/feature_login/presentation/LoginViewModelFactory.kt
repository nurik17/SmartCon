package kz.cifron.smartcon.feature_login.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.cifron.smartcon.feature_login.domain.LoginRepository

class LoginViewModelFactory(private val repository: LoginRepository, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}