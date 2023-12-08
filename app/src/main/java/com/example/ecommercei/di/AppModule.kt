package com.example.ecommercei.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.ecommercei.utils.Constants.INTRODUCTION_SHARED_PREF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth () = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStoreDatabase () = Firebase.firestore
    @Provides
    fun provideIntroductionSharedPreferences (application:Application) = application.getSharedPreferences(INTRODUCTION_SHARED_PREF,MODE_PRIVATE)

}