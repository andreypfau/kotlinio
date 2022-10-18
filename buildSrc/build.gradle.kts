plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version "1.6.21"
}

repositories {
    mavenLocal()
    maven("https://plugins.gradle.org/m2")
}

dependencies {
    implementation(kotlin("gradle-plugin", "1.7.20"))
}
