plugins {
    id("common-conventions")
    id("dependencies")
}

dependencies {
    implementation(project(":libs:modulith"))
    implementation(project(":entities-module:entities-api"))
    implementation(project(":auditlog-module:auditlog-api"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.test { environment("SPRING_CONFIG_NAME", "restapi") }
