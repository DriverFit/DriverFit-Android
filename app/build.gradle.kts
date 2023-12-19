plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "id.ac.unri.driverfit"
    compileSdk = 34

    defaultConfig {
        applicationId = "id.ac.unri.driverfit"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
        mlModelBinding = true
        viewBinding = true
    }
}

dependencies {

    // AndroidX dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // AndroidX Lifecycle dependencies
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // AndroidX navigation dependencies
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    // Material Design dependencies
    implementation("com.google.android.material:material:1.11.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Hilts DI
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    kapt("com.google.dagger:hilt-android-compiler:2.49")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    // TensorFlow Lite dependencies
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.4")

    // Glide dependencies
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // Splash Screen Api
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Swipe View and Idicator for Onboarding
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("me.relex:circleindicator:2.1.6")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Retrofit, Moshi and OkHttp dependencies
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")

    // CameraX dependencies
    val cameraxVersion = "1.3.1"
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}