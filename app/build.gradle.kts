plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("D:\\Keystore\\keystore.jks")
            storePassword = "stravinsky9"
            keyAlias = "keydirza"
            keyPassword = "stravinsky9"
        }
        create("release") {
            storeFile = file("D:\\Keystore\\keystore.jks")
            storePassword = "stravinsky9"
            keyAlias = "keydirza"
            keyPassword = "stravinsky9"
        }
    }
    namespace = "com.dirzaaulia.formula1"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dirzaaulia.formula1"
        minSdk = 29
        targetSdk = 36
        versionCode = project.findProperty("VERSION_CODE")?.toString()?.toIntOrNull() ?: 2
        versionName = project.findProperty("VERSION_NAME")?.toString() ?: "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Accompanist
    implementation(libs.accompanist.system.ui.controller)

    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //Chucker
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)

    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    //Ktor
    implementation(libs.bundles.ktor)

    //Paging
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.compose)


    //Material Icon
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)

    //Shimmer Compose
    implementation(libs.shimmer.compose)
}