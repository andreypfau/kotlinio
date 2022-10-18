import org.gradle.api.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*

val Project.kotlin: KotlinMultiplatformExtension get() = the()

fun Project.kotlin(block: KotlinMultiplatformExtension.() -> Unit) {
    configure(block)
}
