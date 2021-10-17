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
        compileOnly(Libs.Arrow.core)
      }
    }

    val commonTest by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(Libs.KotlinX.coroutines)
        implementation(Libs.Kotest.engine)
        implementation(Libs.Kotest.api)
        implementation(Libs.Kotest.property)
      }
    }

    val jvmTest by getting {
      dependsOn(commonTest)
      dependsOn(jvmMain)
      dependencies {
        implementation(Libs.Kotest.junit5)
        implementation(Libs.Arrow.core)
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

animalsniffer {
  ignore = listOf("java.lang.*")
}

apply("./publish-mpp.gradle.kts")
