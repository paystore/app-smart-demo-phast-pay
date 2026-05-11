package com.phoebus.demo.phastpay.ui.features.main

import androidx.lifecycle.ViewModel
import com.phoebus.demo.phastpay.ui.navigation.NavDestination
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val phastPayClient: PhastPayClient,
    val json: Json
): ViewModel() {

    private val _startRoute = MutableStateFlow<NavDestination?>(null)
    val startRoute = _startRoute.asStateFlow()

    fun isAppPhastPayInstalled(): Boolean{
       return  phastPayClient.isPhastPayAppInstalled()   }

    fun onEvent(event: MainEvent){
        when(event){
            MainEvent.CheckStartRoute -> {
                _startRoute.value = if(isAppPhastPayInstalled()){
                    NavDestination.Home
                }else {
                    NavDestination.CheckInstalled
                }
            }
        }
    }
}