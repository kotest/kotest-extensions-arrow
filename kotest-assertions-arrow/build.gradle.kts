plugins {
  id("kotest-conventions")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        compileOnly(libs.arrow.core)
        implementation(libs.kotest.assertions.core)
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(libs.arrow.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.property)
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
