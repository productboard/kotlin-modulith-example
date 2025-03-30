plugins {
    id("common-conventions")
    id("dependencies")
    id("org.springframework.boot")
}

allprojects {
    group = "com.productboard"
    version = "0.0.0"
}

dependencies {
    implementation(project(":entities-module:entities"))
    implementation(project(":restapi-module:restapi"))
    implementation(project(":auditlog-module:auditlog"))
    implementation(project(":libs:modulith"))
    implementation("org.springframework.boot:spring-boot")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("ch.qos.logback:logback-classic")
    implementation("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-web")
    testImplementation("org.springframework:spring-webflux")
}

val rootProject = project

springBoot { mainClass = "com.productboard.modulith.ModulithApplicationKt" }
