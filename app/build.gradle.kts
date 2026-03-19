import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlin.serialization)
}
val localProperties = Properties()
localProperties.load(rootProject.file("local.properties").inputStream())
val privateApiUrl: String =
    localProperties.getProperty("PRIVATE_API_URL")
        ?: error("PRIVATE_API_URL not found in local.properties")

android {
    namespace = "com.example.armusic"
    compileSdk = 35
    buildFeatures {
        buildConfig = true
        compose = true
    }

    defaultConfig {
        applicationId = "com.example.armusic"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "TEST", "\"hello\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "PRIVATE_API_URL",
            value = privateApiUrl
        )

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

    kapt {
        correctErrorTypes = true
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
    //MIS IMPLEMENTACIONES
    implementation("androidx.compose.runtime:runtime-livedata:1.7.8")

    //viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    //dagger Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")
    //Coil (para AsyncImage)
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    //Material
    implementation("androidx.compose.material:material:1.7.5")
    implementation("androidx.compose.material:material-icons-core:1.7.8")
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")
    // serilize
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    //navigation
    implementation("androidx.navigation:navigation-compose:2.8.8")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    //Gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    // ExoPlayer Core
    implementation("androidx.media3:media3-exoplayer:1.2.0")

    // UI para ExoPlayer (PlayerView)
    implementation("androidx.media3:media3-ui:1.2.0")

    // Dependencia opcional si usas HLS (para `.m3u8`)
    implementation("androidx.media3:media3-exoplayer-hls:1.2.0")
    // animation
    implementation("androidx.compose.animation:animation:1.6.1")
    //google fonts
    implementation ("androidx.compose.ui:ui-text-google-fonts:1.7.8")
    //MediaSession
    implementation("androidx.media3:media3-session:1.2.0")
    //room
    val room_version = "2.7.1"
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-runtime:$room_version")
    //coroutines for room
    implementation("androidx.room:room-ktx:$room_version")
    //okhttpInterceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    //okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    //ULID GENERATOR
    implementation("com.github.f4b6a3:ulid-creator:5.2.2")



}


