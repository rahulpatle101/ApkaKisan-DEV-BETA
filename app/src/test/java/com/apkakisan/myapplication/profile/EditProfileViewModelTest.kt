package com.apkakisan.myapplication.profile

import com.apkakisan.myapplication.network.FirebaseDataSource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class EditProfileViewModelTest {

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var firebaseDataSource: FirebaseDataSource
    private lateinit var editProfileRepository: EditProfileRepository
    private lateinit var editProfileViewModel: EditProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        firebaseDataSource = FirebaseDataSource()
        editProfileRepository = EditProfileRepository(firebaseDataSource)
        editProfileViewModel = EditProfileViewModel(editProfileRepository)
    }

    @Test
    fun `empty name emits UI state EmptyName`(): Unit = runBlocking {
        editProfileViewModel.name = ""
        editProfileViewModel.phone = "3234364949"
        editProfileViewModel.address = "address"
        // specifying Dispatcher.Main is a must
        launch(Dispatchers.Main) {
            // first returns the first element emitted by the flow and then cancels flow's collection.
            val emission = editProfileViewModel.uiState.first()
            assertThat(emission).isEqualTo(EditProfileUiState.EmptyName)
        }
        // emission must be after consumer statement for shared flow
        editProfileViewModel.validate()
    }

    @Test
    fun `empty phone emits UI state EmptyPhone`(): Unit = runBlocking {
        editProfileViewModel.name = "name"
        editProfileViewModel.phone = ""
        editProfileViewModel.address = "address"
        // specifying Dispatcher.Main is a must
        launch(Dispatchers.Main) {
            // first returns the first element emitted by the flow and then cancels flow's collection.
            val emission = editProfileViewModel.uiState.first()
            assertThat(emission).isEqualTo(EditProfileUiState.EmptyPhone)
        }
        // emission must be after consumer statement for shared flow
        editProfileViewModel.validate()
    }

    @Test
    fun `phone less than 10 digits emits UI state InvalidPhone`(): Unit = runBlocking {
        editProfileViewModel.name = "name"
        editProfileViewModel.phone = "32343"
        editProfileViewModel.address = "address"
        // specifying Dispatcher.Main is a must
        launch(Dispatchers.Main) {
            // first returns the first element emitted by the flow and then cancels flow's collection.
            val emission = editProfileViewModel.uiState.first()
            assertThat(emission).isEqualTo(EditProfileUiState.InvalidPhone)
        }
        // emission must be after consumer statement for shared flow
        editProfileViewModel.validate()
    }

    @Test
    fun `phone greater than 10 digits emits UI state InvalidPhone`(): Unit = runBlocking {
        editProfileViewModel.name = "name"
        editProfileViewModel.phone = "32343649499"
        editProfileViewModel.address = "address"
        // specifying Dispatcher.Main is a must
        launch(Dispatchers.Main) {
            // first returns the first element emitted by the flow and then cancels flow's collection.
            val emission = editProfileViewModel.uiState.first()
            assertThat(emission).isEqualTo(EditProfileUiState.InvalidPhone)
        }
        // emission must be after consumer statement for shared flow
        editProfileViewModel.validate()
    }

    @Test
    fun `empty address emits UI state EmptyAddress`(): Unit = runBlocking {
        editProfileViewModel.name = "name"
        editProfileViewModel.phone = "3234364949"
        editProfileViewModel.address = ""
        // specifying Dispatcher.Main is a must
        launch(Dispatchers.Main) {
            // first returns the first element emitted by the flow and then cancels flow's collection.
            val emission = editProfileViewModel.uiState.first()
            assertThat(emission).isEqualTo(EditProfileUiState.EmptyAddress)
        }
        // emission must be after consumer statement for shared flow
        editProfileViewModel.validate()
    }

    @Test
    fun `name, phone and address validated emits state DataValidated`(): Unit = runBlocking {
        editProfileViewModel.name = "name"
        editProfileViewModel.phone = "3234364949"
        editProfileViewModel.address = "address"
        // specifying Dispatcher.Main is a must
        launch(Dispatchers.Main) {
            // first returns the first element emitted by the flow and then cancels flow's collection.
            val emission = editProfileViewModel.uiState.first()
            assertThat(emission).isEqualTo(EditProfileUiState.DataValidated)
        }
        // emission must be after consumer statement for shared flow
        editProfileViewModel.validate()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}