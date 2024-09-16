plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.cat.catiptv"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cat.catiptv"
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

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

// Fragment library
    implementation("androidx.fragment:fragment-ktx:1.6.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    // OkHttp for HTTP requests
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // ExoPlayer core
    implementation ("com.google.android.exoplayer:exoplayer:2.19.0")

    // ExoPlayer UI (for PlayerView)
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.0")

    // ExoPlayer HLS support
    implementation ("com.google.android.exoplayer:exoplayer-hls:2.19.0")

    // Optional: Add additional ExoPlayer modules if needed
    // ExoPlayer DASH support
    implementation ("com.google.android.exoplayer:exoplayer-dash:2.19.0")

    // ExoPlayer SmoothStreaming support
    implementation ("com.google.android.exoplayer:exoplayer-smoothstreaming:2.19.0")
}