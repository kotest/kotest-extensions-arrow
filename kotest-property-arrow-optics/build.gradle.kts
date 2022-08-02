plugins {
  id("kotest-conventions")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        compileOnly(libs.arrow.optics)
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.api)
        implementation(libs.kotest.property)
        implementation(libs.kotlinx.coroutines.core)
        api(projects.kotestPropertyArrow)
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(projects.kotestAssertionsArrow)
        implementation(libs.arrow.optics)
        implementation(libs.kotest.framework.api)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.property)
        implementation(libs.kotlinx.coroutines.core)
      }
    }

    val nativeMain by getting {
      dependencies {
        implementation(libs.arrow.optics)
      }
    }
  }
}

apply(from = "../publish-mpp.gradle.kts")
