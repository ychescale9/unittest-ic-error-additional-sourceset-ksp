plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":annotations"))

    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.0-1.0.9")
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}