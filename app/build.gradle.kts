plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    id("kotlin-parcelize")
//    alias("kotlin-kapt")
}

android {
    namespace = "dev.antasource.goling"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.antasource.goling"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"https://antasource.online/api/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
        dataBinding= true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

//    retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(platform(libs.okhttp.bom))

    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)
    implementation(libs.okhttp3.logging.interceptor)
    implementation (libs.logging.interceptor)

    implementation(libs.kotlinx.coroutines.android)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.security.crypto)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.circleimageview)
    implementation (libs.androidx.fragment.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}