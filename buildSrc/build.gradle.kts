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
