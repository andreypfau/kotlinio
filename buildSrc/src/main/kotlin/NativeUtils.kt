import org.gradle.api.*
import java.io.File

val Project.files: Array<File> get() = project.projectDir.listFiles() ?: emptyArray()
val Project.hasCommon: Boolean get() = files.any { it.name == "common" }
val Project.hasJvm: Boolean get() = hasCommon || files.any { it.name == "jvm" }
val Project.hasJs: Boolean get() = hasCommon || files.any { it.name == "js" }
val Project.hasMingw: Boolean get() = hasCommon || files.any { it.name == "mingw" }
val Project.hasLinux: Boolean get() = hasCommon || files.any { it.name == "linux" }
val Project.hasMacos: Boolean get() = hasCommon || files.any { it.name == "macos" }
val Project.hasIos: Boolean get() = hasCommon || files.any { it.name == "ios" }
val Project.hasTvos: Boolean get() = hasCommon || files.any { it.name == "tvos" }
val Project.hasWatchos: Boolean get() = hasCommon || files.any { it.name == "watchos" }

val Project.hasDarwin: Boolean get() = hasMacos || hasIos || hasTvos || hasWatchos || files.any { it.name == "darwin" }
val Project.hasNix: Boolean get() = hasLinux || hasDarwin || files.any { it.name == "nix" }
val Project.hasPosix: Boolean get() = hasMingw || hasNix || files.any { it.name == "posix" }
val Project.hasNative: Boolean get() = hasPosix || files.any { it.name == "native" }

fun Project.nativeTargets(): List<String> =
    posixTargets()

fun Project.posixTargets(): List<String> =
    mingwTargets() + nixTargets()

fun Project.nixTargets(): List<String> =
    linuxTargets() + darwinTargets()

fun Project.darwinTargets(): List<String> =
    macosTargets() + iosTargets() + watchosTargets() + tvosTargets()

fun Project.mingwTargets(): List<String> = with(kotlin) {
    listOf(
        mingwX64(),
        mingwX86()
    ).map { it.name }
}

fun Project.linuxTargets(): List<String> = with(kotlin) {
    listOf(
        linuxX64(),
        linuxArm64(),
        linuxArm32Hfp()
    ).map { it.name }
}

fun Project.macosTargets(): List<String> = with(kotlin) {
    listOf(
        macosX64(),
        macosArm64()
    ).map { it.name }
}

fun Project.iosTargets(): List<String> = with(kotlin) {
    listOf(
        iosX64(),
        iosArm64(),
        iosArm32(),
        iosSimulatorArm64(),
    ).map { it.name }
}

fun Project.tvosTargets(): List<String> = with(kotlin) {
    listOf(
        tvosX64(),
        tvosArm64(),
        tvosSimulatorArm64(),
    ).map { it.name }
}

fun Project.watchosTargets(): List<String> = with(kotlin) {
    listOf(
        watchosX64(),
        watchosArm32(),
        watchosArm64(),
        watchosSimulatorArm64(),
    ).map { it.name }
}
