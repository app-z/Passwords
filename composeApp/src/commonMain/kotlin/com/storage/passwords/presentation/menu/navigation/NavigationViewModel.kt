package com.storage.passwords.presentation.menu.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.storage.passwords.repository.LocalRepository
import com.storage.passwords.repository.NetworkRepository
import com.storage.passwords.usecase.LoadFromInternetUseCase
import com.storage.passwords.utils.Const.PASSWORD_ID_PARAM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NavigationViewModel(
    val navController: NavController,
    val loadFromInternetUseCase: LoadFromInternetUseCase
) : ViewModel() {

    private val _currentRoute = MutableStateFlow(Screen.Home.route)
    val currentRoute = _currentRoute.asStateFlow()

    fun navigateToRoute(route: String) {
        viewModelScope.launch {
            navController.navigate(route)
            _currentRoute.emit(route)
        }
    }

    fun popBackStackToHome(clearKey: String = PASSWORD_ID_PARAM) {
        viewModelScope.launch {
            navController.previousBackStackEntry?.savedStateHandle?.remove<String>(clearKey)
            navController.popBackStack()
            _currentRoute.emit(Screen.Home.route)
        }
    }

    fun reloadPasswords() {
        viewModelScope.launch {
            loadFromInternetUseCase.loadFromInternet(onLoadSuccess= {

            },
                onLoadError = {

                }
            )
        }
    }

}