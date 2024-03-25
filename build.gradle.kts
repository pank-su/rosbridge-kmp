import com.github.thoebert.krosbridgecodegen.KROSBridgeCodegenPluginConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.androidLibrary)
    id("io.github.thoebert.krosbridge-codegen") version "1.0.6"
    `maven-publish`
}

group = "su.pank"
version = "1.0.6"

val osName = System.getProperty("os.name")
val hostOs = when {
    osName == "Mac OS X" -> "macos"
    osName.startsWith("Win") -> "windows"
    osName.startsWith("Linux") -> "linux"
    else -> error("Unsupported OS: $osName")
}

val osArch = System.getProperty("os.arch")
var hostArch = when (osArch) {
    "x86_64", "amd64" -> "x64"
    "aarch64" -> "arm64"
    else -> error("Unsupported arch: $osArch")
}

val host = "${hostOs}-${hostArch}"

var skiaVersion = "0.0.0-SNAPSHOT"
if (project.hasProperty("skiko.version")) {
    skiaVersion = project.properties["skiko.version"] as String
}

val resourcesDir = "$buildDir/resources"
val skikoWasm by configurations.creating

val isCompositeBuild = extra.properties.getOrDefault("skiko.composite.build", "") == "1"

val unzipTask = tasks.register("unzipWasm", Copy::class) {
    destinationDir = file(resourcesDir)
    from(skikoWasm.map { zipTree(it) })

    if (isCompositeBuild) {
        val skikoWasmJarTask = gradle.includedBuild("skiko").task(":skikoWasmJar")
        dependsOn(skikoWasmJarTask)
    }
}

dependencies {
    if (isCompositeBuild) {
        val filePath = gradle.includedBuild("skiko").projectDir
            .resolve("./build/libs/skiko-wasm-$skiaVersion.jar")
        skikoWasm(files(filePath))
    } else {
        skikoWasm("org.jetbrains.skiko:skiko-js-wasm-runtime:$skiaVersion")
    }
}


kotlin {


    jvm()
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    js(IR) {
        moduleName = "krosbridge-js"
        browser {
            commonWebpackConfig {
                outputFileName = "krosbridge-js.js"
            }
        }
        binaries.executable()
    }

    //@OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            testTask {
                enabled = false
            }

        }
        nodejs {
            testTask {
                enabled = false
            }
        }
        binaries.executable()
    }

    sourceSets {

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }


        jvmMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

        jvmTest {

            dependencies {
                implementation(libs.jakarta.json)
                implementation(libs.jul.to.slf4j)
                implementation(libs.log4j.core)
                implementation(libs.log4j.slf4j.impl)
                implementation(libs.tyrus.server)
                implementation(libs.tyrus.container.grizzly.server)
                implementation(libs.jackson.dataformat.yaml) // YAML Config
                implementation(libs.jackson.module.kotlin)
                implementation("org.jetbrains.skiko:skiko-awt-runtime-$hostOs-$hostArch:$skiaVersion")

            }
        }

        commonMain {
            kotlin.srcDirs("${buildDir}/generated/source/ros")
            dependencies {
                implementation(libs.napier)
                // implementation(libs.kim)
                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.client)
                implementation(libs.ktor.client.logging)
                // implementation(libs.ktor.client.content.neogation)
                // implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.coroutines)
                implementation("org.jetbrains.skiko:skiko:$skiaVersion")

            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.coroutines.test)

            }

        }
        val wasmJsMain by getting {
            dependsOn(commonMain.get())
            resources.setSrcDirs(resources.srcDirs)
            resources.srcDirs(unzipTask.map { it.destinationDir })
        }
        val wasmJsTest by getting {
            dependsOn(wasmJsMain)
            resources.setSrcDirs(resources.srcDirs)
            resources.srcDirs(unzipTask.map { it.destinationDir })
        }
        /*val jsWasmMain by creating {
            dependsOn(commonMain.get())
            resources.setSrcDirs(resources.srcDirs)
            resources.srcDirs(unzipTask.map { it.destinationDir })
        }
        val wasmJsMain by getting {
            dependsOn(jsWasmMain)
        }
        val jsMain by getting {
            dependsOn(jsWasmMain)
        }*/
    }


}


configure<KROSBridgeCodegenPluginConfig> {
    packageName.set("com.github.thoebert.krosbridge.messages")
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().nodeVersion = "16.0.0"
}

project.tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile::class.java).configureEach {
    kotlinOptions.freeCompilerArgs += listOf("-Xir-dce-runtime-diagnostic=log")
}

rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = project.name
            version = version
        }
    }
}

android {
    namespace = "com.github.thoebert.krosbridge"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}