import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
    //id("com.jfrog.bintray")
}

val composeVersion: String by extra { "1.0.1" }
val kotlinVersion: String by extra { "1.5.21" }

android {
    compileSdk = 31
    buildToolsVersion = "31.0.0"
    defaultConfig {
//        applicationId = "com.patchself.compose.navigator"
        minSdk = 28
        targetSdk = 31

//        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
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
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

tasks.register("sourcesJar",Jar::class.java){
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

val libVersion = "0.1.6"
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
//    bintray {
//        user = project.findProperty("bintrayUser") as? String ?: System.getenv("BINTRAY_USER")
//        key = project.findProperty("bintrayApiKey") as? String ?: System.getenv("BINTRAY_API_KEY")
//        setPublications("Production")
//        override = true
//        pkg = PackageConfig().apply {
//            repo = "Compose"
//            name = "compose-navigator"
//            description = "Jetpack compose navigator, page backstack"
//            publish = true
//            publicDownloadNumbers = true
//            setLicenses("Apache-2.0")
//            vcsUrl = "https://github.com/Modificator/compose-navigator.git"
//            dryRun = false
//            version = VersionConfig().apply {
//                name = libVersion
//                desc = "Upgrade compose version"
//
//                vcsTag = libVersion
//            }
//        }
//    }
}
