package com.bryson.testapp.presentation.registrration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryson.testapp.domain.usecase.RegisterUseCase
import com.bryson.testapp.presentation.states.RegistrationState
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registrationState = MutableLiveData<RegistrationState>()
    val registrationState: LiveData<RegistrationState> = _registrationState

    fun register(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _registrationState.value =
                RegistrationState.Error(EMPTY_USER)
        } else {
            viewModelScope.launch {
                try {
                    val isSuccess = registerUseCase.execute(username, password)
                    if (isSuccess) {
                        _registrationState.value = RegistrationState.Success
                    } else {
                        _registrationState.value = RegistrationState.Error(USER_EXIST)
                    }
                } catch (e: Exception) {
                    _registrationState.value = RegistrationState.Error("$ERROR ${e.message}")
                }
            }
        }
    }

    companion object {
        private const val EMPTY_USER = "Username or Password cannot be empty"
        private const val USER_EXIST = "User already exists"
        private const val ERROR = "An error occurred:"
    }
}
