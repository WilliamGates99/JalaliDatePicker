@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.xeniac.jalalidatepickerdemo"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        applicationId = "com.xeniac.jalalidatepickerdemo"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = " - Debug"
            applicationIdSuffix = ".dev.debug"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            ndk.debugSymbolLevel = "FULL" // Include native debug symbols file in app bundle

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        // Java 8+ API Desugaring Support
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }

    kotlinOptions {
        jvmTarget = "22"
    }

    bundle {
        language {
            /*
            Specifies that the app bundle should not support configuration APKs for language resources.
            These resources are instead packaged with each base and dynamic feature APK.
             */
            enableSplit = false
        }
    }
}

dependencies {
    implementation(project(":jalalidatepicker"))

    // Java 8+ API Desugaring Support
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.bundles.essentials)

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    // Architectural Components
    implementation(libs.bundles.architectural.components)

    // Coroutines
    implementation(libs.bundles.coroutines)

    // Timber Library
    implementation(libs.timber)

    // Local Unit Test Libraries
    testImplementation(libs.bundles.local.unit.tests)

    // Instrumentation Test Libraries
    androidTestImplementation(libs.bundles.instrumentation.tests)

    // UI Test Libraries
    androidTestImplementation(libs.bundles.ui.tests)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.compose.ui.test.manifest)
}