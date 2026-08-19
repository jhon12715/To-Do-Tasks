plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    // Hilt
    id("dagger.hilt.android.plugin")
    //KSP
    id("com.google.devtools.ksp")
    //Parcelize
    id("kotlin-parcelize")
    //Json
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.todotasks"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.todotasks"
        minSdk = 24
        targetSdk = 34
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    }

    kotlinOptions {
       jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // WorkManager (Kotlin + coroutines)
    implementation(libs.work.runtime.ktx)

    //DataStore
    implementation(libs.datastore)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}