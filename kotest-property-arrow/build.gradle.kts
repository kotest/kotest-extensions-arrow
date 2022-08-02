plugins {
  id("kotest-conventions")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        compileOnly(libs.arrow.core)
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.api)
        implementation(libs.kotest.property)
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(projects.kotestAssertionsArrow)
        implementation(libs.arrow.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.property)
        implementation(libs.kotlinx.coroutines.core)
      }
    }

    val nativeMain by getting {
      dependencies {
        implementation(libs.arrow.core)
      }
    }
  }
}

apply(from = "../publish-mpp.gradle.kts")
