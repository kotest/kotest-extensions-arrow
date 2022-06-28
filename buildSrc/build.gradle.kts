import org.gradle.kotlin.dsl.`kotlin-dsl`

buildscript {
  repositories {
    mavenCentral()
  }
}

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
