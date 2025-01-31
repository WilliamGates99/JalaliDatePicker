plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
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
}

dependencies {
    // Java 8+ API Desugaring Support
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.bundles.essentials)

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
}