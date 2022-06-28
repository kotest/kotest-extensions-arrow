import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

kotlin {
  explicitApi()

  targets {
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

    iosArm32()
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
    watchosX86()
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common:1.7.0")
        implementation("io.kotest:kotest-assertions-core:5.3.1")
        implementation("io.kotest:kotest-framework-api:5.3.2")
        implementation("io.kotest:kotest-property:5.3.2")
        compileOnly("io.arrow-kt:arrow-core:1.1.2")
      }
    }

    val commonTest by getting {
      dependsOn(commonMain)
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
        implementation("io.kotest:kotest-framework-engine:5.3.2")
        implementation("io.kotest:kotest-framework-api:5.3.2")
        implementation("io.kotest:kotest-property:5.3.2")
        implementation("io.arrow-kt:arrow-core:1.1.2")
        implementation(projects.kotestAssertionsArrow)
      }
    }

    val jvmMain by getting {
      dependsOn(commonMain)
    }

    val jvmTest by getting {
      dependsOn(commonTest)
      dependsOn(jvmMain)
      dependencies {
        implementation("io.kotest:kotest-runner-junit5-jvm:5.3.1")
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
      dependsOn(commonMain)
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
      dependencies {
        implementation("io.arrow-kt:arrow-core:1.1.2")
      }
    }

    all {
      languageSettings.optIn("kotlin.RequiresOptIn")
    }
  }
}

tasks.named<Test>("jvmTest") {
  maxParallelForks = Runtime.getRuntime().availableProcessors()
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
