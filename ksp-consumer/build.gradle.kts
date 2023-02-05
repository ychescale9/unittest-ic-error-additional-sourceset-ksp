plugins {
    id("com.android.library")
    id("com.apollographql.apollo3")
    id("com.google.devtools.ksp")
    kotlin("android")
}

apollo {
    service("dogService") {
        packageNamesFromFilePaths()
    }
}

android {
    namespace = "com.example.icbug"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDirs("$buildDir/generated/source/apollo")
        }
    }
}

tasks.withType<com.google.devtools.ksp.gradle.KspTask>().configureEach {
     dependsOn(tasks.named("generateApolloSources"))
}

dependencies {
    implementation(project(":annotations"))
    ksp(project(":test-processor"))

    implementation("com.apollographql.apollo3:apollo-runtime")

    testImplementation("junit:junit:4.13.2")
}