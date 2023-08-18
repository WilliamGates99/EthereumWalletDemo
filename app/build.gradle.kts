import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

val properties = gradleLocalProperties(rootDir)

android {
    namespace = "com.xeniac.ethereumwalletdemo"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = "com.xeniac.ethereumwalletdemo"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        /*
        Keeps language resources for only the locales specified below.
         */
        resourceConfigurations.addAll(listOf("en-rUS"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/database_schemas",
                    "room.incremental" to "true"
                )
            }
        }

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = " - debug"
            applicationIdSuffix = ".debug"
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3") // Material Design 3
    implementation("androidx.compose.runtime:runtime-livedata") // Compose Integration with LiveData
    implementation("androidx.activity:activity-compose:1.7.2") // Compose Integration with Activities
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0") // Compose Navigation Integration with Hilt
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1") // Compose Constraint Layout

    // Android Studio Compose Preview Support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.0")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-compiler:2.47")

    // Architectural Components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1") // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1") // ViewModel Utilities for Compose
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1") // Lifecycles Only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1") // Lifecycle Utilities for Compose

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Room Library
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2") // Kotlin Extensions and Coroutines support for Room
    ksp("androidx.room:room-compiler:2.5.2")

    // Timber Library
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Web3j Library
    implementation("org.web3j:core:5.0.0")

    // Local Unit Test Libraries
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0") // Test Helpers for LiveData
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.room:room-testing:2.5.2")

    // Instrumentation Test Libraries
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}