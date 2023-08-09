// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val composeVersion by extra("1.4.3")
    val kotlinVersion: String by extra { "1.9.0" }

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        //classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}