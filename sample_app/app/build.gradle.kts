plugins {
    id("com.android.application")
}

android {
    namespace = "com.tnkfactory.tmat.offerer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tnkfacotry.tmat.offerer"
        minSdk = 21
        targetSdk = 35
        versionCode = 101
        versionName = "1.0.1"
    }
    buildTypes {
        release {
            // Tnk SDK 8.09.05 부터 앱 ProGuard/R8 설정에 Tnk 규칙을 넣지 않는다(SDK 에 포함됨).
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Tnk tmat SDK
    implementation("com.tnkfactory:tmat:8.09.05")
}
