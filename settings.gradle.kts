rootProject.name = "kotest-extensions-arrow"

pluginManagement {
   repositories {
      mavenCentral()
      gradlePluginPortal()
   }
}

include("kotest-assertions-arrow", "kotest-property-arrow", "kotest-property-arrow-optics")
