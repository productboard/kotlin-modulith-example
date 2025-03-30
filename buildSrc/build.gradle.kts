import org.gradle.kotlin.dsl.repositories

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

val testcontainersVersion: String by project

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.1.10")
    implementation("org.jetbrains.kotlin.plugin.spring:org.jetbrains.kotlin.plugin.spring.gradle.plugin:2.1.10")
    implementation("org.jetbrains.kotlin.plugin.jpa:org.jetbrains.kotlin.plugin.jpa.gradle.plugin:2.1.10")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
    implementation("com.ncorti.ktfmt.gradle:com.ncorti.ktfmt.gradle.gradle.plugin:0.22.0")
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
}
