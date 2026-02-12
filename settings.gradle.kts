pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.google.devtools.ksp") version "2.3.4" apply false
        id("com.google.dagger.hilt.android") version "2.59.1" apply false
        id("org.jetbrains.kotlin.android") version "2.2.20" apply false
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Android-Bootcamp-2026-Frontend"
include(":app")
include(":comon")
include(":navigation")
include(":authorization")
include(":token_storage")
include(":registration")
include(":user_main")
include(":meetings")
