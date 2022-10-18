rootProject.name = "kotlinio"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("multiplatform") version "1.7.20"
    }
}

include(":kotlinio-base64")
include(":kotlinio-hex")
include(":kotlinio-pool")
include(":kotlinio-crypto")
include(":kotlinio-memory")
