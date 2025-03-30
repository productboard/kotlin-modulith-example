@file:Suppress("UnstableApiUsage")

import com.ncorti.ktfmt.gradle.tasks.KtfmtBaseTask
import org.gradle.api.internal.artifacts.dependencies.ProjectDependencyInternal
import org.gradle.internal.component.external.model.TestFixturesSupport.TEST_FIXTURE_SOURCESET_NAME
import org.gradle.util.Path as GradlePath
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.jetbrains.kotlin.plugin.jpa")
    id("com.ncorti.ktfmt.gradle")
    id("com.adarshr.test-logger")
}

// specifies both compatibility version for Java sources and target JVM bytecode version
val javaVersion = JavaVersion.VERSION_21

java.sourceCompatibility = javaVersion

ktfmt {
    kotlinLangStyle()
    removeUnusedImports.set(true)
    maxWidth.set(120)
}

tasks.withType<KtfmtBaseTask> {
    exclude {
        // don't check nor format generated code
        it.file.path.contains("generated/")
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JvmTarget.fromTarget(javaVersion.toString())
    }
}

testing {
    suites {
        withType(JvmTestSuite::class.java) {
            useJUnitJupiter()

            // make sure all tests see the project dependencies
            configurations.named(sources.implementationConfigurationName).get()
                .extendsFrom(configurations.implementation.get())

            // make sure all additional test source sets see the main code and test code
            if (sources.name != sourceSets.test.name) {
                sources {
                    compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
                    runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
                }
            }

            targets {
                all {
                    testTask {
                        maxHeapSize = "768m"

                        testlogger {
                            showStandardStreams = true
                            showPassedStandardStreams = false
                            showSkippedStandardStreams = false
                            showFailedStandardStreams = true
                            showFullStackTraces = true
                            slowThreshold = 5000
                        }
                    }
                }
            }
        }
    }
}

// Checks that modules only depend on each others API
val checkProjectDependencies by tasks.registering {
    doFirst {
        if (project.parent == null) {
            // Ignore root project
            return@doFirst
        }

        fun ProjectDependencyInternal.projectPathSegments() = targetProjectIdentity.projectPath.segments()
        // the Modulith "module" is the Gradle "project", i.e. the first segment of the path
        fun GradlePath.moduleName() = segments().first()

        val deps = project
            .configurations
            .asSequence()
            .filterNot { it.name.contains("test", ignoreCase = true) } // allow for tests
            .flatMap { conf ->
                conf.dependencies
                    .asSequence()
                    .filterIsInstance<ProjectDependencyInternal>() // only take internal dependencies
                    .filterNot { it.projectPathSegments().first() == "libs" } // Allow :libs
                    .filterNot { it.projectPathSegments().last().endsWith("-api") } // Allow API
                    .filterNot {
                        // allow depending on other modules in the same project (Gradle lingo), i.e.
                        // allow depending on other sub-modules in the same module (Modulith lingo)
                        it.targetProjectIdentity.projectPath.moduleName() == GradlePath.path(project.path).moduleName()
                    }
            }.toSet()
        require(deps.isEmpty()) {
            "Project $project depends on implementation of another module which is not allowed $deps"
        }
    }
}

tasks.check {
    dependsOn(checkProjectDependencies)
}
