import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

repositories {
  mavenCentral()
  maven {
    url = uri("https://oss.sonatype.org/content/repositories/snapshots")
  }
}

plugins {
  java
  id("java-library")
  id("maven-publish")
  signing
  kotlin("multiplatform").version(Libs.kotlinVersion)
  id("org.jetbrains.dokka") version Libs.dokkaVersion
  id("io.kotest.multiplatform") version "5.0.0.5"
  id("ru.vyarus.animalsniffer").version("1.5.3")
}

kotlin {
  explicitApi()

  targets {
    jvm {
      compilations.all {
        kotlinOptions {
          jvmTarget = "1.8"
        }
      }
    }
  }
}

allprojects {

  group = Libs.org
  version = Ci.version


  tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
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

  fun KotlinTarget.compilerArgs(): Unit =
    compilations.all {
      kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
          "-Xskip-runtime-version-check",
          "-Xopt-in=kotlin.RequiresOptIn",
          "-Xextended-compiler-checks"
        )
      }
    }

  animalsniffer {
    ignore = listOf("java.lang.*")
  }
}

apply("./publish-mpp.gradle.kts")
