package com.storage.passwords.presentation.menu.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NavigationViewModel(val navController: NavController) : ViewModel() {


    private val _currentRoute = MutableStateFlow(Screen.Home.route)
    val currentRoute = _currentRoute.asStateFlow()


    fun navigateToRoute(route: String) {
        viewModelScope.launch {
            navController.navigate(route)
            _currentRoute.emit(route)
        }
    }

    fun popBackStackToHome() {
        viewModelScope.launch {
            navController.popBackStack()
            _currentRoute.emit(Screen.Home.route)
        }
    }


}