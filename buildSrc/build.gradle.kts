import org.gradle.kotlin.dsl.`kotlin-dsl`

buildscript {
  repositories {
    mavenCentral()
    maven {
      url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
  }
}

repositories {
  mavenCentral()
  gradlePluginPortal()
}

plugins {
  `kotlin-dsl`
}
