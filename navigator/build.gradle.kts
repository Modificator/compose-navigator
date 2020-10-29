import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
    id("com.jfrog.bintray")
}

val composeVersion: String by extra { "1.0.0-alpha05" }
android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.2"
    defaultConfig {
//        applicationId = "com.patchself.compose.navigator"
        minSdkVersion(29)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled =  false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
        kotlinCompilerVersion = "1.4.10"
    }
}

dependencies {
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.ui:ui-tooling:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.0-beta01")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

tasks.register("sourcesJar",Jar::class.java){
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

val libVersion = "0.1.0"
val libGroup = "com.patchself"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("Production") {
                from(components.getByName("release"))
                artifact(tasks.getByName("sourcesJar"))
                groupId = libGroup
                artifactId = "compose-navigator"
                version = libVersion

                pom {
                    groupId = libGroup
                    artifactId = "compose-navigator"
                    version = libVersion
                    description.set("Jetpack compose navigator, page backstack")
                    url.set("https://github.com/Modificator/compose-navigator")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("Modificator")
                            name.set("Modificator")
                            email.set("yunshangcn@gmail.com")
                        }
                    }
                }
            }
        }
    }
    bintray {
        user = ""
        key = ""
        setPublications("Production")
        override = true
        pkg = PackageConfig().apply {
            repo = "Compose"
            name = "compose-navigator"
            description = "Jetpack compose navigator, page backstack"
            publish = true
            publicDownloadNumbers = true
            setLicenses("Apache-2.0")
            vcsUrl = "https://github.com/Modificator/compose-navigator.git"
            dryRun = false
            version = VersionConfig().apply {
                name = libVersion
                desc = "Jetpack compose navigator, page backstack"

                vcsTag = libVersion
            }
        }
    }
}