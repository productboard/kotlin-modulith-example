plugins {
    id("common-conventions")
    id("dependencies")
}

dependencies {
    implementation(project(":libs:modulith"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
}
