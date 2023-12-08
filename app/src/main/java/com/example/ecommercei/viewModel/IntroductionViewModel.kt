package com.example.ecommercei.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercei.R
import com.example.ecommercei.utils.Constants.INTRODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth : FirebaseAuth
)
    :ViewModel() {

    private val _navigateState = MutableStateFlow(0)
    val navigateState:StateFlow<Int> = _navigateState
    companion object{
        const val SHOPPING_ACTIVITY = 100
         val ACCOUNT_OPTIONS_FRAGMENT = R.id.action_introductionFragment_to_accountOptionsFragment
    }
        init {
            val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY,false)
            val user = firebaseAuth.currentUser

            if (user!=null){
                viewModelScope.launch {
                    _navigateState.emit(SHOPPING_ACTIVITY)
                }
            }else if(isButtonClicked){
                viewModelScope.launch {
                    _navigateState.emit(ACCOUNT_OPTIONS_FRAGMENT)
                }
            }else{
                Unit
            }

        }

    fun startButtonClick(){
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY,true).apply()
    }

}