
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":kotlinio-pool"))
                implementation(project(":kotlinio-hex"))
            }
        }
    }
}
