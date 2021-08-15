import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

repositories {
  mavenCentral()
  maven {
    url = uri("https://oss.sonatype.org/content/repositories/snapshots")
  }
}

plugins {
  java
  `java-library`
  id("java-library")
  id("maven-publish")
  signing
  `maven-publish`
  kotlin("multiplatform").version(Libs.kotlinVersion)
}

group = Libs.org
version = Ci.version

kotlin {
  explicitApi()

  targets {

    jvm {
      withJava()
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += listOf(
            "-Xskip-runtime-version-check",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xextended-compiler-checks"
          )
          jvmTarget = "1.8"
        }
      }
    }

    js(BOTH) {
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
        compileOnly(Libs.Kotest.property)
        compileOnly(Libs.Arrow.fx)
      }
    }

    val commonTest by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(Libs.KotlinX.coroutines)
        implementation(Libs.Kotest.engine)
        implementation(Libs.Arrow.fx)
        implementation(Libs.Kotest.property)
      }
    }
  }
}


tasks.named<Test>("jvmTest") {
  useJUnitPlatform()
  testLogging {
    showExceptions = true
    showStandardStreams = true
    exceptionFormat = TestExceptionFormat.FULL
    events = setOf(
      TestLogEvent.FAILED,
      TestLogEvent.PASSED
    )
    exceptionFormat = TestExceptionFormat.FULL
  }
}

apply("./publish.gradle.kts")
