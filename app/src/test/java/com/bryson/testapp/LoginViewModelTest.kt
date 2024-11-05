package com.bryson.testapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bryson.testapp.domain.repository.UserRepository
import com.bryson.testapp.domain.usecase.LoginUseCase
import com.bryson.testapp.presentation.login.LoginViewModel
import com.bryson.testapp.presentation.states.LoginState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    @Mock
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var userRepository: UserRepository

    private lateinit var loginViewModel: LoginViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userRepository = Mockito.mock(UserRepository::class.java)
        val userUseCase = LoginUseCase(userRepository)
        loginViewModel = LoginViewModel(userUseCase)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {

        Dispatchers.resetMain()
    }

    @Test
    fun `login succeeds with correct credentials`() = runTest {
        val username = "zxc"
        val password = "zxc"
        `when`(userRepository.authenticate(username, password)).thenReturn(true)

        loginViewModel.login(username, password)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = loginViewModel.loginState.getOrAwaitValue()
        assertTrue(state is LoginState.Success)
    }


    @Test
    fun `login fails with incorrect credentials`() = runTest {
        val username = "wrongUser"
        val password = "wrongPassword"
        `when`(userRepository.authenticate(username, password)).thenReturn(false)

        loginViewModel.login(username, password)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = loginViewModel.loginState.getOrAwaitValue()
        assertTrue(state is LoginState.Error)
        assertEquals("Invalid credentials", (state as LoginState.Error).message)
    }

    @Test
    fun `login throws exception and LoginState is Error`() = runTest {
        val username = "user"
        val password = "pass"
        `when`(
            userRepository.authenticate(
                username,
                password
            )
        ).thenThrow(RuntimeException("Server error"))

        loginViewModel.login(username, password)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = loginViewModel.loginState.getOrAwaitValue()
        assertTrue(state is LoginState.Error)
        assertEquals("An error occurred: Server error", (state as LoginState.Error).message)
    }
}