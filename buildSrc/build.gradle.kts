import org.gradle.kotlin.dsl.`kotlin-dsl`

repositories {
  mavenCentral()
  gradlePluginPortal()
}

plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(libs.kotlin.gradle.plugin)
  implementation(libs.animalsniffer)
}
