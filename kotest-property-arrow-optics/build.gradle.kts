plugins {
  id("kotest-conventions")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        compileOnly(libs.arrow.optics)
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.api)
        implementation(libs.kotest.property)
        implementation(libs.kotlinx.coroutines.core)
        api(projects.kotestPropertyArrow)
      }
    }

    commonTest {
      dependencies {
        implementation(projects.kotestAssertionsArrow)
        implementation(libs.arrow.optics)
        implementation(libs.kotest.framework.api)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.property)
        implementation(libs.kotlinx.coroutines.core)
      }
    }

    nativeMain {
      dependencies {
        implementation(libs.arrow.optics)
      }
    }
  }
}

apply(from = "../publish-mpp.gradle.kts")
