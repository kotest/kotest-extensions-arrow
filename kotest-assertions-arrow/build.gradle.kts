plugins {
  id("kotest-conventions")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        compileOnly(libs.arrow.core)
        implementation(libs.kotest.assertions.core)
      }
    }

    commonTest {
      dependencies {
        implementation(libs.arrow.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.property)
      }
    }

    jsMain {
      dependencies {
        api(libs.arrow.core)
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
