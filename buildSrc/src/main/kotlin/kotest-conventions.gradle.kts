import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.all
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named

plugins {
  `java-library`
  kotlin("multiplatform")
  id("ru.vyarus.animalsniffer")
}

repositories {
  mavenCentral()
  mavenLocal()
  gradlePluginPortal()
}

group = "io.kotest.extensions"
version = Ci.version

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
    val commonMain by getting
    val commonTest by getting {
      dependsOn(commonMain)
    }

    val jvmMain by getting {
      dependsOn(commonMain)
    }

    val jvmTest by getting {
      dependsOn(commonTest)
      dependsOn(jvmMain)
      dependencies {
        implementation("io.kotest:kotest-runner-junit5-jvm:5.3.0")
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

    val nativeMain by creating {
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
    }

    val mingwX64Test by getting
    val linuxX64Test by getting
    val iosArm32Test by getting
    val iosArm64Test by getting
    val iosSimulatorArm64Test by getting
    val iosX64Test by getting
    val macosArm64Test by getting
    val macosX64Test by getting
    val tvosArm64Test by getting
    val tvosSimulatorArm64Test by getting
    val tvosX64Test by getting
    val watchosArm32Test by getting
    val watchosArm64Test by getting
    val watchosSimulatorArm64Test by getting
    val watchosX64Test by getting
    val watchosX86Test by getting

    create("nativeTest") {
      dependsOn(nativeMain)
      dependsOn(commonTest)
      mingwX64Test.dependsOn(this)
      linuxX64Test.dependsOn(this)
      iosArm32Test.dependsOn(this)
      iosArm64Test.dependsOn(this)
      iosSimulatorArm64Test.dependsOn(this)
      iosX64Test.dependsOn(this)
      macosArm64Test.dependsOn(this)
      macosX64Test.dependsOn(this)
      tvosArm64Test.dependsOn(this)
      tvosSimulatorArm64Test.dependsOn(this)
      tvosX64Test.dependsOn(this)
      watchosArm32Test.dependsOn(this)
      watchosArm64Test.dependsOn(this)
      watchosSimulatorArm64Test.dependsOn(this)
      watchosX64Test.dependsOn(this)
      watchosX86Test.dependsOn(this)
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
