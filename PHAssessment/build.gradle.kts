// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0-RC" apply false
    id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id ("com.android.library") version "8.0.2" apply false
}
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.7.0")
        // Other dependencies...
    }
}