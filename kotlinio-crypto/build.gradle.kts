import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":kotlinio-pool"))
                implementation(project(":kotlinio-hex"))
            }
        }
    }

    targets.configureEach {
        if (this !is KotlinNativeTarget) return@configureEach
        compilations.configureEach {
            cinterops {
                val libgcrypt by creating {
                }
            }
        }
    }

    macosArm64() {
        binaries {
            executable()
        }
    }
}
