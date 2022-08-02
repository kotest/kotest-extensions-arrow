plugins {
  id("kotest-conventions")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        compileOnly(libs.arrow.fx.coroutines)
        api(projects.kotestAssertionsArrow)
        api(libs.kotlinx.coroutines.core)
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.property)
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(libs.arrow.fx.coroutines)
      }
    }

    val nativeMain by getting {
      dependencies {
        implementation(libs.arrow.fx.coroutines)
      }
    }
  }
}

apply(from = "../publish-mpp.gradle.kts")
