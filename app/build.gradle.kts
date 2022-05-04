plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("android")
    kotlin("kapt")
}


android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.appId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
        debug {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }
    compileOptions {
        sourceCompatibility = AppConfig.jdk
        targetCompatibility = AppConfig.jdk
    }
    kotlinOptions {
        jvmTarget = AppConfig.jdk.toString()
    }
    /**
     * Decide to get rid of koin ksp
     */
//    applicationVariants.all {
//        sourceSets {
//            forEach {
//                it.java.srcDir("build/generated/ksp/${it.name}/kotlin")
//            }
//        }
//    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
        )
    }
}

dependencies {
    implementation(project(Modules.Base.COMMON))
    implementation(project(Modules.Base.DATA))
    implementation(project(Modules.Libs.RETROFIT_API))
    implementation(project(Modules.Base.COMMON))

    // Logging
    debugImplementation(Libs.Logging.CHUCKER_DEBUG)
    releaseImplementation(Libs.Logging.CHUCKER_RELEASE)

    // Navigation
    implementation(Libs.Navigation.UI)
    implementation(Libs.Navigation.FRAGMENT)

    //Features
    implementation(project(Modules.Features.AUTH))
    implementation(project(Modules.Features.USER_PREFERENCES))
    implementation(project(Modules.Features.MAIN))
    implementation(project(Modules.Features.RECIPE))

    implementation(Libs.Network.Serializer.MOSHI)
    implementation(Libs.Network.Serializer.MOSHI_CONVERTER)
    implementation(Libs.Network.Serializer.MOSHI_KOTLIN)
    implementation(Libs.Network.Serializer.MOSHI_ADAPTERS)
    implementation(Libs.Network.Serializer.MOSHI_CODEGEN)
}