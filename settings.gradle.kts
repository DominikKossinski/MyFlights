pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven {
            url = uri("https://jitpack.io")
        }
        jcenter()
        mavenCentral()
        gradlePluginPortal()
    }
}


rootProject.name = "MyFlights"
include(":app")

