plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.erpproject"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.erpproject"
        minSdk = 34
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.room:room-runtime:2.8.3")
    implementation(libs.fragment)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor ("androidx.room:room-compiler:2.8.3")



    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.9.4")
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.9.4")
    implementation("com.google.android.material:material:1.12.0")
    implementation("at.favre.lib:bcrypt:0.10.2")

}