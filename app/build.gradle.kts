import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.example.pokedexapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.pokedexapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.inputStream())
    }

    fun getProperty(key: String, defaultValue: String = ""): String {
        return properties.getProperty(key) ?: defaultValue
    }

    flavorDimensions += "environment"
    productFlavors {
        create("development") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

            buildConfigField("String", "POKEMON_API_BASE_URL", "\"${getProperty("POKEMON_API_BASE_URL")}\"")
            buildConfigField("String", "UUID", "\"${getProperty("UUID")}\"")
            buildConfigField("String", "ENVIRONMENT", "\"development\"")

            resValue("string", "app_name", "PokeDex Dev")
        }

        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"

            buildConfigField(
                "String",
                "POKEMON_API_BASE_URL",
                "\"${getProperty("POKEMON_API_BASE_URL")}\""
            )
            buildConfigField("String", "POKEMON_API_BASE_URL", "\"${getProperty("POKEMON_API_BASE_URL")}\"")
            buildConfigField("String", "UUID", "\"${getProperty("UUID")}\"")
            buildConfigField("String", "ENVIRONMENT", "\"staging\"")

            resValue("string", "app_name", "PokeDex Staging")
        }

        create("production") {
            dimension = "environment"

            buildConfigField(
                "String",
                "POKEMON_API_BASE_URL",
                "\"${getProperty("POKEMON_API_BASE_URL")}\""
            )
            buildConfigField("String", "POKEMON_API_BASE_URL", "\"${getProperty("POKEMON_API_BASE_URL")}\"")
            buildConfigField("String", "UUID", "\"${getProperty("UUID")}\"")
            buildConfigField("String", "ENVIRONMENT", "\"production\"")

            resValue("string", "app_name", "PokeDex")
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.material3)
    implementation(libs.androidx.palette.ktx)
    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.library.no.op)
}