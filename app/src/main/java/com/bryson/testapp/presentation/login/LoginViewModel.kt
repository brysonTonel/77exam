package com.bryson.testapp.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryson.testapp.domain.usecase.LoginUseCase
import com.bryson.testapp.presentation.states.LoginState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error(EMPTY_USER)
        } else {
            viewModelScope.launch {
                try {
                    val isSuccess = loginUseCase.execute(username, password)
                    if (isSuccess) {
                        _loginState.value = LoginState.Success
                    } else {
                        _loginState.value = LoginState.Error(INVALID_CREDENTIALS)
                    }
                } catch (e: Exception) {
                    _loginState.value =
                        LoginState.Error("$ERROR ${e.message}")
                }

            }
        }
    }

    companion object {
        private const val EMPTY_USER = "Username or Password cannot be empty"
        private const val INVALID_CREDENTIALS = "Invalid credentials"
        private const val ERROR = "An error occurred:"
    }
}
