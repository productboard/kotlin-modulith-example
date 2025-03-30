plugins {
    id("common-conventions")
    id("dependencies")
}

dependencies {
    implementation(project(":libs:modulith"))
    implementation(project(":entities-module:entities-api"))
    implementation(project(":auditlog-module:auditlog-api"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.testcontainers:testcontainers")
    implementation("org.testcontainers:postgresql")
    implementation("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test { environment("SPRING_CONFIG_NAME", "entities") }
