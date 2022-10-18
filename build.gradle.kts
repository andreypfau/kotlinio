plugins {
    kotlin("multiplatform")
    `maven-publish`
}

allprojects {
    apply(plugin = "kotlin-multiplatform")
    apply(plugin = "maven-publish")

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
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                }
            }
            val jvmMain by getting
            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test-junit5"))
                    implementation("org.pcap4j:pcap4j-distribution:1.8.2")
                }
            }
            val nativeMain by creating {
                dependsOn(commonMain)
            }

            if (isMacos) {
                macosX64()
                macosArm64()
                iosArm64()
                tvosArm64()
                watchosArm64()

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
                linuxArm64()

                val linuxMain by creating {
                    dependsOn(nativeMain)
                }
                val linuxX64Main by getting {
                    dependsOn(linuxMain)
                }
                val linuxArm64Main by getting {
                    dependsOn(linuxMain)
                }
            }
        }
    }
}

kotlin {
    targets.filterIsInstance(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).forEach {
        it.compilations {
            getByName("main") {
                cinterops {
                    val bits by creating {
                        defFile(file("src/nativeMain/interop/bits.def"))
                    }
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                subprojects {
                    implementation(this)
                }
            }
        }
    }
}
