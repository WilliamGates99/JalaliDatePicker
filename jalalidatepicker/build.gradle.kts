plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.xeniac.jalalidatepicker"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        compose = true
    }

    compileOptions {
        // Java 8+ API Desugaring Support
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }

    kotlinOptions {
        jvmTarget = "23"
    }
}

dependencies {
    // Java 8+ API Desugaring Support
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.bundles.library.essentials)

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.library.compose)
}