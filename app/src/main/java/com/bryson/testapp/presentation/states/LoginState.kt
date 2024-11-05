package com.bryson.testapp.presentation.states


sealed class LoginState {
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}