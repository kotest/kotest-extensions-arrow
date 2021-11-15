import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

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
        compileOnly("org.jetbrains.kotlin:kotlin-stdlib-common:1.5.31")
        compileOnly("io.kotest:kotest-assertions-shared:5.0.0.M3")
        compileOnly("io.kotest:kotest-assertions-core:5.0.0.M3")
        compileOnly("io.kotest:kotest-framework-api:5.0.0.706-SNAPSHOT")
      }
    }

    val jvmMain by getting {
      dependsOn(commonMain)
      dependencies {
        compileOnly("io.arrow-kt:arrow-core:1.0.1")
      }
    }

    val commonTest by getting {
      dependsOn(commonMain)
      dependencies {
        implementation("io.kotest:kotest-framework-engine:5.0.0.M3")
        implementation("io.kotest:kotest-framework-api:5.0.0.M3")
        implementation("io.kotest:kotest-property:5.0.0.M3")
      }
    }

    val jvmTest by getting {
      dependsOn(commonTest)
      dependsOn(jvmMain)
      dependencies {
        implementation("io.kotest:kotest-runner-junit5-jvm:5.0.0.M3")
        implementation("io.arrow-kt:arrow-core:1.0.1")
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

animalsniffer {
  ignore = listOf("java.lang.*")
}

apply(from = "../publish-mpp.gradle.kts")

