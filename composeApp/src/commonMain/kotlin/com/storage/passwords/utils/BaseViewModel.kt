package com.storage.passwords.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.utils.UiText
import com.storage.passwords.presentation.detail.DetailEffect
import com.storage.passwords.presentation.detail.DetailState
import com.storage.passwords.presentation.passwords.PasswordsEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.unknown_error


interface ViewEvent

interface ViewState

interface ViewSideEffect

abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewSideEffect> : ViewModel() {

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception: Throwable ->
        viewModelScope.launch {
            onCoroutineException(
                if (exception.message != null)
                    UiText.StaticString(exception.message!!)
                else
                    UiText.StringResource(Res.string.unknown_error)
            )
        }
    }

    abstract fun onCoroutineException(message: UiText)

    val defaultViewModelScope = CoroutineScope(SupervisorJob() + coroutineExceptionHandler)

    abstract fun setInitialState(): UiState
    abstract fun runInitialEvent()
    abstract fun handleEvents(event: Event)

    private val initialState: UiState by lazy { setInitialState() }

    private val _viewState: MutableStateFlow<UiState> = MutableStateFlow (initialState)
//    val viewState: StateFlow<UiState> = _viewState.asStateFlow()

    val viewState = _viewState
        .onStart {
            runInitialEvent()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _viewState.value
        )

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()


    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        defaultViewModelScope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }

    fun setEvent(event: Event) {
        defaultViewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        defaultViewModelScope.launch { _effect.emit(effectValue) }
    }


}