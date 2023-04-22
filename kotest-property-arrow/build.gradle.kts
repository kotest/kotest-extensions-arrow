plugins {
  id("kotest-conventions")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        compileOnly(libs.arrow.core)
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.api)
        implementation(libs.kotest.property)
      }
    }

    commonTest {
      dependencies {
        implementation(projects.kotestAssertionsArrow)
        implementation(libs.arrow.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.property)
        implementation(libs.kotlinx.coroutines.core)
      }
    }

    nativeMain {
      dependencies {
        implementation(libs.arrow.core)
      }
    }
  }
}

apply(from = "../publish-mpp.gradle.kts")
