package com.storage.passwords.presentation.menu.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.spacex.utils.UiText
import com.storage.passwords.usecase.LoadFromInternetUseCase
import com.storage.passwords.utils.Const.PASSWORD_ID_PARAM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val navController: NavController,
    private val loadFromInternetUseCase: LoadFromInternetUseCase
) : ViewModel() {

    private val _currentRoute = MutableStateFlow(NavigationState())
    val currentState = _currentRoute.asStateFlow()

    fun navigateToRoute(route: String) {
        viewModelScope.launch {
            navController.navigate(route)
            _currentRoute.update {
                it.copy(route = route)
            }
        }
    }

    fun popBackStackToHome(clearKey: String = PASSWORD_ID_PARAM) {
        viewModelScope.launch {
            navController.previousBackStackEntry?.savedStateHandle?.remove<String>(clearKey)
            navController.popBackStack()
            _currentRoute.emit(
                NavigationState(route = Screen.Home.route)
            )
        }
    }

    fun reloadPasswords() {
        viewModelScope.launch {
            isShowProgress(true)
            loadFromInternetUseCase.loadFromInternet(
                onLoadSuccess = {
                    isShowProgress(false)
                },
                onLoadError = {
                    isShowProgress(false,
                        error = UiText.StaticString(it ?: "Unknown error" )
                    )
                }
            )
        }
    }

    private fun isShowProgress(isShow: Boolean, error: UiText? = null) {
        _currentRoute.update {
            it.copy(isShowProgress = isShow, error = error)
        }
    }

}