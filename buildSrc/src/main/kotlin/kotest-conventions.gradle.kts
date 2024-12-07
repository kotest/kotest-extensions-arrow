import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.named

plugins {
  kotlin("multiplatform")
  id("ru.vyarus.animalsniffer")
}

repositories {
  mavenCentral()
  gradlePluginPortal()
}

group = "io.kotest.extensions"
version = Ci.version

kotlin {
  explicitApi()

  metadata()

  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "1.8"
    }
  }

  js(IR) {
    browser()
    nodejs()
  }

  linuxX64()

  mingwX64()

  iosArm64()
  iosSimulatorArm64()
  iosX64()
  macosArm64()
  macosX64()
  tvosArm64()
  tvosSimulatorArm64()
  tvosX64()
  watchosArm32()
  watchosArm64()
  watchosSimulatorArm64()
  watchosX64()

  applyDefaultHierarchyTemplate()

  sourceSets {
    val kotestVersion = resolveVersion("kotest")

    val jvmTest by getting {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
      }
    }

    all {
      languageSettings.optIn("kotlin.RequiresOptIn")
    }
  }
}

animalsniffer {
  ignore = listOf("java.lang.*")
}

tasks.named<Test>("jvmTest") {
  useJUnitPlatform()
  maxParallelForks = Runtime.getRuntime().availableProcessors()
  testLogging {
    showExceptions = true
    showStandardStreams = true
    events = setOf(
      TestLogEvent.FAILED,
      TestLogEvent.PASSED
    )
    exceptionFormat = TestExceptionFormat.FULL
  }
}
