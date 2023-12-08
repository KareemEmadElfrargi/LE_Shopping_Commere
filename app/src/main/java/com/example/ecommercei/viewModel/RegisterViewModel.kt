package com.example.ecommercei.viewModel

import androidx.lifecycle.ViewModel
import com.example.ecommercei.data.User
import com.example.ecommercei.utils.Constants.USER_COLLECTION
import com.example.ecommercei.utils.RegisterFieldState
import com.example.ecommercei.utils.RegisterValidation
import com.example.ecommercei.utils.Resource
import com.example.ecommercei.utils.validationEmail
import com.example.ecommercei.utils.validationPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseFirestore

):ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register :Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()
    fun createAccountWithEmailAndPassword(user: User,password:String) {

        if (checkValidation(user, password)) {

            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid,user)
                    }

                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        } else {
            val registerFieldState = RegisterFieldState(
                validationEmail(user.email), validationPassword(password)
            )
            runBlocking {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun saveUserInfo(dataUid: String,user:User) {
        database.collection(USER_COLLECTION)
            .document(dataUid)
            .set(user)
            .addOnSuccessListener {
               _register.value = Resource.Success(user)

            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())

            }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validationEmail(user.email)
        val passwordValidation = validationPassword(password)

        return emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
    }
}