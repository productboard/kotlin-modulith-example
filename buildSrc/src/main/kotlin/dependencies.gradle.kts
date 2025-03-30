import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

plugins {
    id("io.spring.dependency-management")
    id("java")
}

val springBootVersion: String by project
val testcontainersVersion: String by project

val versions = mapOf(
    "springBoot" to springBootVersion,
    "testcontainers" to testcontainersVersion,
)

dependencyManagement {
    repositories {
        mavenCentral()
    }
    // Disable Maven-style exclusion behavior. This is not needed for the build to work and seems to cause
    // noticeable slowdowns of the build.
    applyMavenExclusions(false)

    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${versions["springBoot"]}")
        mavenBom("org.testcontainers:testcontainers-bom:${versions["testcontainers"]}")
    }

    dependencies {
        /* Logging & monitoring */
        dependency("io.github.microutils:kotlin-logging:3.0.5")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.microutils:kotlin-logging")
}
