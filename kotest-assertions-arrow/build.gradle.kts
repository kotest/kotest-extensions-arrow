import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

kotlin {
  explicitApi()

  targets {
    metadata {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }

    jvm {
      compilations.all {
        kotlinOptions {
          jvmTarget = "1.8"
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }

    js(IR) {
      browser()
      nodejs()
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }

    linuxX64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }

    mingwX64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }

    iosArm32 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    iosArm64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    iosSimulatorArm64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    iosX64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    macosArm64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    macosX64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    tvosArm64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    tvosSimulatorArm64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    tvosX64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    watchosArm32 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    watchosArm64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    watchosSimulatorArm64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    watchosX64 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
      }
    }
    watchosX86 {
      compilations.all {
        kotlinOptions {
          freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
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
        compileOnly(Libs.Kotest.api)
        compileOnly(Libs.Arrow.core)
      }
    }

    val commonTest by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(Libs.Kotest.engine)
        implementation(Libs.Kotest.api)
        implementation(Libs.Kotest.property)
        implementation(Libs.Arrow.core)
      }
    }

    val jvmMain by getting {
      dependsOn(commonMain)
    }

    val jvmTest by getting {
      dependsOn(commonTest)
      dependsOn(jvmMain)
      dependencies {
        implementation(Libs.Kotest.junit5)
      }
    }

    val jsMain by getting {
      dependsOn(commonMain)
    }

    val jsTest by getting {
      dependsOn(commonTest)
      dependsOn(jsMain)
    }

    val mingwX64Main by getting
    val linuxX64Main by getting
    val iosArm32Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    val iosX64Main by getting
    val macosArm64Main by getting
    val macosX64Main by getting
    val tvosArm64Main by getting
    val tvosSimulatorArm64Main by getting
    val tvosX64Main by getting
    val watchosArm32Main by getting
    val watchosArm64Main by getting
    val watchosSimulatorArm64Main by getting
    val watchosX64Main by getting
    val watchosX86Main by getting

    create("nativeMain") {
      dependencies {
        implementation(Libs.stdLib)
        implementation(Libs.Kotest.assertionsShared)
        implementation(Libs.Kotest.assertionsCore)
        implementation(Libs.Kotest.api)
        implementation(Libs.Arrow.core)
      }

      mingwX64Main.dependsOn(this)
      linuxX64Main.dependsOn(this)
      iosArm32Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      iosSimulatorArm64Main.dependsOn(this)
      iosX64Main.dependsOn(this)
      macosArm64Main.dependsOn(this)
      macosX64Main.dependsOn(this)
      tvosArm64Main.dependsOn(this)
      tvosSimulatorArm64Main.dependsOn(this)
      tvosX64Main.dependsOn(this)
      watchosArm32Main.dependsOn(this)
      watchosArm64Main.dependsOn(this)
      watchosSimulatorArm64Main.dependsOn(this)
      watchosX64Main.dependsOn(this)
      watchosX86Main.dependsOn(this)
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

animalsniffer {
  ignore = listOf("java.lang.*")
}

apply(from = "../publish-mpp.gradle.kts")
