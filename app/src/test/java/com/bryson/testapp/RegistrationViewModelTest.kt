package com.bryson.testapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bryson.testapp.domain.repository.UserRepository
import com.bryson.testapp.domain.usecase.RegisterUseCase
import com.bryson.testapp.presentation.registrration.RegistrationViewModel
import com.bryson.testapp.presentation.states.RegistrationState
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
class RegistrationViewModelTest {

    @get:Rule
    @Mock
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var userRepository: UserRepository

    private lateinit var registrationViewModel: RegistrationViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userRepository = Mockito.mock(UserRepository::class.java)
        val registrationUseCase = RegisterUseCase(userRepository)
        registrationViewModel = RegistrationViewModel(registrationUseCase)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {

        Dispatchers.resetMain()
    }

    @Test
    fun `registration succeeds for new user`() = runTest {
        val username = "new"
        val password = "new"
        `when`(userRepository.registerUser(username, password)).thenReturn(true)

        registrationViewModel.register(username, password)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = registrationViewModel.registrationState.getOrAwaitValue()
        assertTrue(state is RegistrationState.Success)
    }


    @Test
    fun `registration fails for existing user`() = runTest {
        val username = "existingUser"
        val password = "existingPassword"
        `when`(userRepository.registerUser(username, password)).thenReturn(false)

        registrationViewModel.register(username, password)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = registrationViewModel.registrationState.getOrAwaitValue()
        assertTrue(state is RegistrationState.Error)
        assertEquals("User already exists", (state as RegistrationState.Error).message)
    }

    @Test
    fun `registration throws exception`() = runTest {
        val username = "exception"
        val password = "exception"
        `when`(
            userRepository.registerUser(
                username,
                password
            )
        ).thenThrow(RuntimeException("Server error"))

        registrationViewModel.register(username, password)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = registrationViewModel.registrationState.getOrAwaitValue()
        assertTrue(state is RegistrationState.Error)
        assertEquals("An error occurred: Server error", (state as RegistrationState.Error).message)
    }
}