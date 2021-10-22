import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
  mavenCentral()
  maven {
    url = uri("https://oss.sonatype.org/content/repositories/snapshots")
  }
}

plugins {
  id("maven-publish")
  signing
  kotlin("multiplatform").version(Libs.kotlinVersion) apply false
  id("org.jetbrains.dokka") version Libs.dokkaVersion
  id("io.kotest.multiplatform") version "5.0.0.5"
  id("ru.vyarus.animalsniffer") version "1.5.3"
}

allprojects {
  group = Libs.org
  version = Ci.version

  repositories {
    mavenCentral()
    maven {
      url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
    gradlePluginPortal()
  }

  apply(plugin = "ru.vyarus.animalsniffer")
  // apply(plugin = "java")
  // apply(plugin = "java-library")
  apply(plugin = "io.kotest.multiplatform")
  apply("$rootDir/publish-mpp.gradle.kts")
  apply(plugin = "maven-publish")

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.apiVersion = "1.5"
  }
}

subprojects {
  apply(plugin = "org.jetbrains.kotlin.multiplatform")
}
