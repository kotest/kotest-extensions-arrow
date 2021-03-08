object Libs {

   const val kotlinVersion = "1.4.31"
   const val org = "io.kotest.extensions"

   object Kotest {
      private const val version = "4.4.3"
      const val assertionsShared = "io.kotest:kotest-assertions-shared:$version"
      const val assertionsCore = "io.kotest:kotest-assertions-core:$version"
      const val api = "io.kotest:kotest-framework-api:$version"
      const val junit5 = "io.kotest:kotest-runner-junit5-jvm:$version"
   }

   object Arrow {
      private const val version = "0.11.0"
      const val core = "io.arrow-kt:arrow-core:$version"
      const val fx = "io.arrow-kt:arrow-fx:$version"
      const val syntax = "io.arrow-kt:arrow-syntax:$version"
      const val validation = "io.arrow-kt:arrow-validation:$version"
   }
}
