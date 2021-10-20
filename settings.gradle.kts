rootProject.name = "kotest-extension-arrow"

pluginManagement {
   repositories {
      mavenCentral()
      gradlePluginPortal()
   }
}

include("kotest-assertions-arrow-core", "kotest-property-arrow-core")
