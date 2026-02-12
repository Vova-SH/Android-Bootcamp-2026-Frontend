import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.plugin.serialization)
}

android {
    namespace = "ru.sicampus.bootcamp2026"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.sicampus.bootcamp2026"
        minSdk = 25
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }
    buildFeatures {
        compose = true
    }
    /*composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }*/
}

dependencies {
    // навигация compose 2.9
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // интерация Compose и Activity
    implementation(libs.androidx.activity.compose)
    // BOM - управляем версиями compose автоматически
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(platform(libs.androidx.compose.bom))
    // Базовый UI Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    // Preview
    implementation(libs.androidx.ui.tooling.preview)
    // M3
    implementation(libs.androidx.material3)

    implementation(libs.coil.compose)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.coil)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil3.coil.network.ktor3)
    implementation(libs.androidx.compose.foundation)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // для @Preview
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // для доп иконок
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
}