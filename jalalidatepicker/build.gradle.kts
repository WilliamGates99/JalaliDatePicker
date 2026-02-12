import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.xeniac.jalalidatepicker"
    compileSdk = 36
    buildToolsVersion = "36.1.0"

    defaultConfig {
        minSdk = 23

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
        resValues = true
    }

    compileOptions {
        // Java 8+ API Desugaring Support
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(target = "21")
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

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.williamgates99"
            artifactId = "jalalidatepicker"
            version = "1.0.5"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}