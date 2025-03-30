rootProject.name = "modulith-example"

pluginManagement {
    val springBootVersion: String by settings
    plugins { id("org.springframework.boot") version springBootVersion }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0" }

include("auditlog-module:auditlog-api")

include("auditlog-module:auditlog")

include("restapi-module:restapi")

include("entities-module:entities-api")

include("entities-module:entities")

include("libs:modulith")
