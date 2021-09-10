object Libs {

  const val kotlinVersion = "1.5.30"
  const val org = "io.kotest.extensions"
  const val dokkaVersion = "0.10.1"
  const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-common:$kotlinVersion"

  object KotlinX {
    private const val version = "1.5.2"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
  }

  object Kotest {
    private const val version = "5.0.0.M1"
    const val assertionsShared = "io.kotest:kotest-assertions-shared:$version"
    const val assertionsCore = "io.kotest:kotest-assertions-core:$version"
    const val property = "io.kotest:kotest-property:$version"
    const val api = "io.kotest:kotest-framework-api:$version"
    const val engine = "io.kotest:kotest-framework-engine:$version"
    const val junit5 = "io.kotest:kotest-runner-junit5-jvm:$version"
  }

  object Arrow {
    private const val version = "1.0.0-SNAPSHOT"
    const val core = "io.arrow-kt:arrow-core:$version"
    const val fx = "io.arrow-kt:arrow-fx-coroutines:$version"
    const val optics = "io.arrow-kt:arrow-optics:$version"
  }
}
