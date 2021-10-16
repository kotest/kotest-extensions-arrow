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

group = Libs.org
version = Ci.version

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

    js(BOTH) {
      compilerArgs()
      browser {
        testTask {
          useKarma {
            useChromeHeadless()
          }
        }
      }
      nodejs {
        testTask {
          useMocha {
            timeout = "600000"
          }
        }
      }
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        compileOnly(Libs.stdLib)
        compileOnly(Libs.Kotest.assertionsShared)
        compileOnly(Libs.Kotest.assertionsCore)
        compileOnly(Libs.KotlinX.coroutines)
        compileOnly(Libs.Kotest.api)
        compileOnly(Libs.Kotest.property)
      }
    }

    val jvmMain by getting {
      dependsOn(commonMain)
      dependencies {
        compileOnly(Libs.Arrow.fx)
        compileOnly(Libs.Arrow.optics)
      }
    }

    val jvmTest by getting {
      dependsOn(jvmMain)
      dependencies {
        implementation(Libs.KotlinX.coroutines)
        implementation(Libs.Kotest.engine)
        implementation(Libs.Arrow.fx)
        implementation(Libs.Kotest.api)
        implementation(Libs.Kotest.property)
        implementation(Libs.Kotest.junit5Runner)
      }
    }
  }
}


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
      freeCompilerArgs += listOf(
        "-Xskip-runtime-version-check",
        "-Xopt-in=kotlin.RequiresOptIn",
        "-Xextended-compiler-checks"
      )
    }
  }

apply("./publish-mpp.gradle.kts")
