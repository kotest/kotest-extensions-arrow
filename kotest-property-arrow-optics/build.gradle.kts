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
        compileOnly(Libs.stdLib)
        compileOnly(Libs.Kotest.assertionsShared)
        compileOnly(Libs.Kotest.assertionsCore)
        compileOnly(Libs.KotlinX.coroutines)
        compileOnly(Libs.Kotest.api)
        compileOnly(Libs.Kotest.property)
        api(project(Libs.Kotest.propertyArrowCore))
      }
    }

    val jvmMain by getting {
      dependsOn(commonMain)
      dependencies {
        compileOnly(Libs.Arrow.optics)
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
        implementation(project(Libs.Kotest.assertionsArrowCore))
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
