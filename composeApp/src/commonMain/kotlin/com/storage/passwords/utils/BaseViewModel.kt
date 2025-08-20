package com.storage.passwords.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.utils.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.unknown_error


interface ViewEvent

interface ViewState

interface ViewSideEffect

abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewSideEffect>
    (initUiState: UiState) : ViewModel() {

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

    abstract fun runInitialEvent()
    abstract fun handleEvents(event: Event)

    private val _viewState: MutableStateFlow<UiState> = MutableStateFlow(initUiState)

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