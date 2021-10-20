import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
  id("java-library") apply true
  id("maven-publish") apply true
  signing
  kotlin("multiplatform").version(Libs.kotlinVersion) apply true
  id("org.jetbrains.dokka") version Libs.dokkaVersion apply true
  id("io.kotest.multiplatform") version "5.0.0.5" apply true
  id("ru.vyarus.animalsniffer").version("1.5.3") apply true
  id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.7.1" apply true
}

allprojects {
  group = Libs.org
  version = Ci.version

  repositories {
    mavenCentral()
    maven {
      url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
  }

  apply(plugin = "org.jetbrains.kotlin.multiplatform")
  apply(plugin = "ru.vyarus.animalsniffer")
  apply("${project.rootDir}/publish-mpp.gradle.kts")
  apply(plugin = "java")
  apply(plugin = "java-library")
  apply(plugin="org.jetbrains.kotlinx.binary-compatibility-validator")

  kotlin {
    explicitApi()

    targets {
      jvm {
        compilations.all {
          kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs += listOf(
              "-Xskip-runtime-version-check",
              "-Xopt-in=kotlin.RequiresOptIn",
              "-Xextended-compiler-checks"
            )
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

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.apiVersion = "1.5"
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

  animalsniffer {
    ignore = listOf("java.lang.*")
  }
}
