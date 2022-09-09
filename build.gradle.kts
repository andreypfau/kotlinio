plugins {
    kotlin("multiplatform") version "1.7.20-Beta"
    `maven-publish`
}

group = "com.github.andreypfau"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    val hostOs = System.getProperty("os.name")
    val allTarget = true
    val isMingw = hostOs.startsWith("Windows")
    val isLinux = hostOs.startsWith("Linux")
    val isMacos = hostOs.startsWith("Mac OS")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        val jvmMain by getting
        val nativeMain by creating {
            dependsOn(commonMain)
        }

        if (isMacos) {
            macosX64()
            macosArm64() {
                binaries {
                    executable()
                }
            }

            val darwinMain by creating {
                dependsOn(nativeMain)
            }
            val macosMain by creating {
                dependsOn(darwinMain)
            }
            val macosX64Main by getting {
                dependsOn(macosMain)
            }
            val macosArm64Main by getting {
                dependsOn(macosMain)
            }
        }

        if (isMingw || allTarget) {
            mingwX64()

            val mingwMain by creating {
                dependsOn(nativeMain)
            }
            val mingwX64Main by getting {
                dependsOn(mingwMain)
            }
        }

        if (isLinux || allTarget) {
            linuxX64()

            val linuxMain by creating {
                dependsOn(nativeMain)
            }
            val linuxX64Main by getting {
                dependsOn(linuxMain)
            }
        }
    }
}
