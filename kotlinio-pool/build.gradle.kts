kotlin {
    js {
        nodejs()
        browser()
    }
    wasm() {
        applyBinaryen()
        d8()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
//                implementation("org.jetbrains.kotlinx:atomicfu:0.18.4")
            }
        }
    }
}
