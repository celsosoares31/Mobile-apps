plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.sosincendios"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sosincendios"
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
    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation (libs.play.services.base)
    implementation (libs.integrity)
    implementation (libs.firebase.core)
    implementation (libs.play.services.auth)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
//    implementation( libs.viewpager2)
    implementation (libs.android.gif.drawable)
    implementation (libs.circleimageview)
    implementation (libs.glide)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.10.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation (libs.material.v190)
    implementation (libs.firebase.storage.v2011)
    implementation (libs.firebase.analytics)
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))

    implementation ("id.zelory:compressor:2.1.1")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    // Add the dependencies for the App Check libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation ("com.google.firebase:firebase-appcheck-playintegrity")

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.google.firebase.auth)

    implementation ("com.github.AnyChart:AnyChart-Android:1.1.5")
}