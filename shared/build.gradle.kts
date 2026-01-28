import co.touchlab.skie.configuration.DefaultArgumentInterop
import co.touchlab.skie.configuration.FlowInterop
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.swift.skie)
    kotlin("plugin.serialization") version "2.2.21"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            optIn.add("kotlin.time.ExperimentalTime")
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = false
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ksoup)

            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite)
            implementation(libs.koin.core)
            implementation(libs.swift.skie.annotations)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.sqlite.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqlite.bundled)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.github.aeoliux.violet.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

skie {
    features {
        group {
            DefaultArgumentInterop.Enabled(true)
            coroutinesInterop.set(true)
            FlowInterop.Enabled(true)
        }
    }
}
