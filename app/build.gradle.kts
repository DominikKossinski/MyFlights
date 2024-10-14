plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safeargs.kotlin)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dragger.hilt)
}


android {
    namespace = "pl.kossa.myflights"
    compileSdk = 35
    defaultConfig {
        applicationId = "pl.kossa.myflights"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "pl.kossa.myflights.CustomTestRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
        buildConfigField("String", "SERVER_URL", "")
        buildConfigField("long", "BUILD_TIMESTAMP", System.currentTimeMillis().toString() + "L")
        buildConfigField("String", "BUILD_ENV", "")
    }

    buildTypes {
        debug {
            /* TODO val localProperties = Properties()
            localProperties.load(project.rootProject.file("local.properties").newDataInputStream())
            val serverUrl = localProperties.getProperty("server_url") ?: "NoServerUrlFound"*/
            val serverUrl = "\"http://localhost:8080/\""
            buildConfigField("String", "SERVER_URL", serverUrl)
            buildConfigField("String", "BUILD_ENV", "\"dev\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            /* TODO val localProperties = Properties()
            localProperties.load(project.rootProject.file("local.properties").newDataInputStream())
            val serverUrl = localProperties.getProperty("server_url") ?: "NoServerUrlFound"*/
            val serverUrl = "\"http://localhost:8080/\""
            buildConfigField("String", "SERVER_URL", serverUrl)
            buildConfigField("long", "BUILD_TIMESTAMP", System.currentTimeMillis().toString() + "L")
            buildConfigField("String", "BUILD_ENV", "\"prod\"")
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    hilt {
        enableTransformForLocalTests = true
    }
}

dependencies {
    implementation(libs.play.services.auth)
    implementation(libs.androidx.swiperefreshlayout)
    kapt(libs.compiler)
    //noinspection GradleDependency
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    androidTestImplementation(libs.androidx.fragment.testing)


    // Kotlin
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Feature module Support
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)

    //Material Components
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v113)
    androidTestImplementation(libs.androidx.espresso.core.v340)
    androidTestImplementation(libs.androidx.espresso.contrib)

    //Scanning QR Code
    implementation(libs.barcode.scanning)
    implementation(libs.play.services.mlkit.barcode.scanning)


    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    testImplementation(libs.androidx.room.testing)


    //CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)


    //Firebase
    implementation(platform(libs.firebase.bom))
    //In app messaging
    implementation(libs.firebase.inappmessaging.display.ktx)
    //Analytics
    implementation(libs.firebase.analytics.ktx)
    //Auth
    implementation(libs.firebase.auth.ktx)
    //Crashlytics
    implementation(libs.firebase.crashlytics.ktx)
    //Performance
    implementation(libs.firebase.perf.ktx)
    //Messaging
    implementation(libs.firebase.messaging.ktx)
    //DynamicLinks
    implementation(libs.firebase.dynamic.links.ktx)
    //Remote config
    implementation(libs.firebase.config.ktx)


    //GSM
    implementation(libs.play.services.basement)


    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp3.okhttp)
    testImplementation(libs.mockwebserver)

    androidTestImplementation(libs.mockwebserver)

    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.fragment)
    kapt(libs.hilt.compiler)

    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.compiler)
    testAnnotationProcessor(libs.dagger.hilt.android.compiler)

    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.dagger.hilt.android.compiler)
    androidTestAnnotationProcessor(libs.dagger.hilt.android.compiler)

    //Glide
    implementation(libs.glide)
    implementation(libs.okhttp3.integration) /*{
        // TODO exclude group:("glide-parent")
    }*/
    annotationProcessor(libs.glide.compiler)
    ksp(libs.glide.ksp)

    //Permissions
    implementation(libs.runtime.permission.kotlin)

    //Charts
    implementation(libs.mpandroidchart)

    //QRCodes
    implementation(libs.android)

}