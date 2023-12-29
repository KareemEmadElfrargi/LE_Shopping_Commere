plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id ("kotlin-parcelize")
    id ("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.ecommercei"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ecommercei"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

  buildFeatures {
      viewBinding = true
  }

}
kapt {
    correctErrorTypes = true
    generateStubs = true
}
dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.49")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //circular image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //Android Ktx
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.0")
    //loading button
    implementation("com.github.leandroborgesferreira:loading-button-android:2.3.0")

    //viewpager2 indicator --.
    implementation ("androidx.viewpager2:viewpager2:view_pager_version")

    //stepView
    //implementation ("com.shuhart.stepview:stepview:1.5.2")
    implementation ("com.github.shuhart:stepview:1.5.1")
    //lottile
    implementation ("com.airbnb.android:lottie:3.5.0")
    //Navigation component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.5")

    //Scalable DP
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

//    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.5.0")
//    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.5.0")
//    implementation ("androidx.core:core-ktx:1.6.0")
//    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    /*    coroutines support for firebase operations */
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.0")
}