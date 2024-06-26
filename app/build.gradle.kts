plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.luka_kuzyk.bakalarskapraca"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.luka_kuzyk.bakalarskapraca"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "1.2.3"

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
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.github.barteksc:android-pdf-viewer:2.8.2")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    implementation ("com.google.firebase:firebase-bom:32.8.0")
    implementation ("com.google.android.gms:play-services-auth:20.5.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.11.0")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    implementation ("com.google.android.material:material:1.2.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5e.1")

}



