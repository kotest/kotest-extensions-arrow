enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "kotest-extensions-arrow"

pluginManagement {
   repositories {
      mavenCentral()
      gradlePluginPortal()
   }
}

include("kotest-assertions-arrow")
include("kotest-property-arrow")
include("kotest-property-arrow-optics")
