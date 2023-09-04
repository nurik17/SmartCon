package kz.cifron.smartcon.presentation.login

import android.accounts.AccountManager.KEY_PASSWORD
import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class LoginViewModel(private val repository: LoginRepository,private val context: Context) : ViewModel() {

    val loginStateLiveData = MutableLiveData<LoginState>()

    fun performLogin(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            loginStateLiveData.postValue(LoginState.Loading)
            try {
                val response = repository.login(email, password)
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        loginStateLiveData.postValue(
                            LoginState.Success(
                                responseData.user,
                                responseData.token
                            )
                        )
                    }
                } else {
                    loginStateLiveData.postValue(LoginState.Error("Login failed: ${response.code()}"))
                }
            } catch (_: Exception) {
            }
        }
    }

    fun saveUserInfo(email: String,password: String){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit()
            .putString("saved_email",email)
            .putString("saved_password",password)
            .apply()
    }
    fun getSavedUserInfo() : Pair<String, String>? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val savedEmail = sharedPreferences.getString("saved_email",null)
        val savedPassword = sharedPreferences.getString("saved_password",null)
        return if(!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()){
            Pair(savedEmail,savedPassword)
        }
        else{ null }
    }
}