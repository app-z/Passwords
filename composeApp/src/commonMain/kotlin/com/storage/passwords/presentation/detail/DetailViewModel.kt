package com.storage.passwords.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    val localRepository: LocalRepository,
    val password_id: String
) : ViewModel() {


    private val _state = MutableStateFlow(DetailState())

    val state = _state.asStateFlow()


    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            val detail = localRepository.getPasswords(password_id)
            _state.emit(
                DetailState(passwordItem = detail.mapToDomain()
                )
            )
        }
    }

}