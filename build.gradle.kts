// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.navigation.safeargs.kotlin) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.dragger.hilt) apply false
}

allprojects {
    repositories {
        google()
        maven {
            url = uri("https://jitpack.io")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
/*buildscript {
   ext.kotlin_version = "1.6.21"
   ext.hilt_version = '2.40.1'
   ext.room_version = '2.4.3'

   repositories {
       google()
       mavenCentral()
       mavenLocal()
       maven { url 'https://jitpack.io' }
   }
   dependencies {
       classpath 'com.android.tools.build:gradle:8.7.0'
       classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
       classpath 'com.google.gms:google-services:4.3.14'
       classpath 'com.google.firebase:firebase-crashlytics-gradle:2.9.2'
       classpath 'com.google.firebase:perf-plugin:1.4.2'
       classpath "androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3"
       classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"


       // NOTE: Do not place your application dependencies here; they belong
       // in the individual module build.gradle files
   }
}

allprojects {
   repositories {
       google()
       mavenCentral()
       mavenLocal()
       maven { url 'https://jitpack.io' }
   }
}

task clean(type: Delete) {
   delete rootProject.buildDir
}*/