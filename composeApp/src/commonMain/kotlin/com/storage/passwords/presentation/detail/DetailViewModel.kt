package com.storage.passwords.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spacex.utils.UiText
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.repository.LocalRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    val password_id: String,
    val localRepository: LocalRepository,
) : ViewModel() {

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception: Throwable ->
        viewModelScope.launch {
            _effect.emit(
                DetailEffect.LoadError(
                    UiText.StaticString(exception.message ?: "Unknown Error")
                )
            )
        }
    }

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DetailEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadDetail()
    }

    fun loadDetail() {

        viewModelScope.launch(coroutineExceptionHandler) {
            _effect.emit(DetailEffect.Loading)

            try {
                val detail = localRepository.getPasswords(password_id)
                _state.emit(
                    DetailState(
                        passwordItem = detail.mapToDomain()
                    )
                )
                _effect.emit(DetailEffect.LoadSuccess)
            } catch (ex: Exception) {
                _effect.emit(
                    DetailEffect.LoadError(
                        UiText.StaticString(ex.message ?: "Unknown Error")
                    )
                )
            }

        }
    }

}