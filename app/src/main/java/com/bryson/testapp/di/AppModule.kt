package com.bryson.testapp.di

import com.bryson.testapp.data.local.AppDatabase
import com.bryson.testapp.data.model.UserRepositoryImpl
import com.bryson.testapp.domain.repository.UserRepository
import com.bryson.testapp.domain.usecase.LoginUseCase
import com.bryson.testapp.domain.usecase.RegisterUseCase
import com.bryson.testapp.presentation.login.LoginViewModel
import com.bryson.testapp.presentation.registrration.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().userDao() }

    single<UserRepository> { UserRepositoryImpl(get()) }

    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
}