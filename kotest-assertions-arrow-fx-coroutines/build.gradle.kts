plugins {
  id("kotest-conventions")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        compileOnly(libs.arrow.fx.coroutines)
        api(projects.kotestAssertionsArrow)
        api(libs.kotlinx.coroutines.core)
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.property)
      }
    }

    commonTest {
      dependencies {
        implementation(libs.arrow.fx.coroutines)
      }
    }

    jsMain {
      dependencies {
        api(libs.arrow.fx.coroutines)
      }
    }

    nativeMain {
      dependencies {
        implementation(libs.arrow.fx.coroutines)
      }
    }
  }
}

apply(from = "../publish-mpp.gradle.kts")
