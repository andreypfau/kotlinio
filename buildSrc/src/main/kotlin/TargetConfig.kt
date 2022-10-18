import org.gradle.api.*
import org.gradle.api.tasks.testing.*
import org.gradle.jvm.tasks.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.*

fun Project.configureTargets() {
    if (hasJvm) configureJvm()
    if (hasJs) configureJs()
    kotlin {
        sourceSets {
            if (hasNative) {
                val commonMain by getting
                val commonTest by getting

                val nativeMain by creating
                val nativeTest by creating

                nativeMain.dependsOn(commonMain)
                nativeTest.dependsOn(commonTest)
            }
            if (hasPosix) {
                val nativeMain by getting
                val nativeTest by getting

                val posixMain by creating
                val posixTest by creating

                posixMain.dependsOn(nativeMain)
                posixTest.dependsOn(nativeTest)
            }
            if (hasNix) {
                val posixMain by getting
                val posixTest by getting

                val nixMain by creating
                val nixTest by creating
                nixMain.dependsOn(posixMain)
                nixTest.dependsOn(posixTest)
            }
            if (hasDarwin) {
                val nixMain by getting
                val nixTest by getting

                val darwinMain by creating
                val darwinTest by creating

                darwinMain.dependsOn(nixMain)
                darwinTest.dependsOn(nixTest)
            }
            if (hasLinux) {
                val nixMain by getting
                val nixTest by getting

                val linuxMain by creating
                val linuxTest by creating

                linuxMain.dependsOn(nixMain)
                linuxTest.dependsOn(nixTest)
            }
            if (hasMingw) {
                val mingwMain by creating
                val mingwTest by creating

                val posixMain by creating
                val posixTest by creating

                mingwMain.dependsOn(posixMain)
                mingwTest.dependsOn(posixTest)
            }
        }
    }
}
