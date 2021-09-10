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
}

group = Libs.org
version = Ci.version

kotlin {
  explicitApi()

  targets {

    jvm {
      withJava()
      compilerArgs()
      compilations.all {
        kotlinOptions {
          jvmTarget = "1.8"
        }
      }
    }

    js(IR) {
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

    linuxX64()

    //mingwX64()

    macosX64()

    //    tvos()
//
//    watchosArm32()
//    watchosArm64()
//    watchosX86()
//    watchosX64()
//
//    iosX64()
//    iosArm64()
//    iosArm32()
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
        compileOnly(Libs.Arrow.fx)
        compileOnly(Libs.Arrow.optics)
      }
    }

    val commonTest by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(Libs.KotlinX.coroutines)
        implementation(Libs.Kotest.engine)
        implementation(Libs.Arrow.fx)
        implementation(Libs.Kotest.api)
        implementation(Libs.Kotest.property)
      }
    }

    val jvmTest by getting {
      dependsOn(commonTest)
      dependencies {
        implementation(Libs.Kotest.junit5)
      }
    }

    val desktopMain by creating {
      dependsOn(commonMain)
    }

    val macosX64Main by getting {
      dependsOn(desktopMain)
    }

//    val mingwX64Main by getting {
//      dependsOn(desktopMain)
//    }

    val linuxX64Main by getting {
      dependsOn(desktopMain)
    }

//    val iosX64Main by getting {
//      dependsOn(desktopMain)
//    }
//
//    val iosArm64Main by getting {
//      dependsOn(desktopMain)
//    }
//
//    val iosArm32Main by getting {
//      dependsOn(desktopMain)
//    }
//
//    val watchosArm32Main by getting {
//      dependsOn(desktopMain)
//    }
//
//    val watchosArm64Main by getting {
//      dependsOn(desktopMain)
//    }
//
//    val watchosX86Main by getting {
//      dependsOn(desktopMain)
//    }
//
//    val watchosX64Main by getting {
//      dependsOn(desktopMain)
//    }
//
//    val tvosMain by getting {
//      dependsOn(desktopMain)
//    }
  }
}


tasks.named<Test>("jvmTest") {
  useJUnitPlatform()
  testLogging {
    showExceptions = true
    showStandardStreams = true
    events = setOf(
      org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
      org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
    )
    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
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
