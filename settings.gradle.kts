rootProject.name = "kotest-extension-arrow"

pluginManagement {
   repositories {
      mavenCentral()
      gradlePluginPortal()
   }
}

include("kotest-assertions-arrow", "kotest-property-arrow", "kotest-property-arrow-optics")
