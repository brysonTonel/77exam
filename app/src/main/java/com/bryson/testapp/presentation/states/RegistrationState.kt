package com.bryson.testapp.presentation.states



sealed class RegistrationState {
    data object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}