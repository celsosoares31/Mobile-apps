plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.roomwordsample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.roomwordsample"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

//    implementation(libs.room.compiler)
    annotationProcessor(libs.room.runtime)
    implementation(libs.room.common)
    implementation(libs.room.runtime)

    androidTestImplementation(libs.room.testing)


    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.common)
    implementation (libs.lifecycle.extensions)

    testImplementation(libs.junit)
    androidTestImplementation(libs.arch.core)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
//    {
//        exclude("com.android.support", "support-annotations")
//    }

}