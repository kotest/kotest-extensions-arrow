import org.gradle.kotlin.dsl.`kotlin-dsl`

repositories {
  mavenLocal()
  gradlePluginPortal()
}

plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(libs.kotlin.gradle.plugin)
  implementation(libs.animalsniffer)
}
