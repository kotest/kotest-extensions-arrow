import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
  mavenCentral()
}

plugins {
  java
  kotlin("multiplatform").version("1.6.20") apply false
  `java-library`
  id("maven-publish")
  signing
  id("org.jetbrains.dokka") version "1.6.20"
  id("io.kotest.multiplatform") version "5.2.3"
  id("ru.vyarus.animalsniffer") version "1.5.4"
}

allprojects {
  group = "io.kotest.extensions"
  version = Ci.version

  repositories {
    mavenCentral()
    gradlePluginPortal()
  }

  apply(plugin = "ru.vyarus.animalsniffer")
  apply(plugin = "java")
  apply(plugin = "java-library")
  apply(plugin = "io.kotest.multiplatform")
  apply(plugin = "maven-publish")

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
  }
}

subprojects {
  apply(plugin = "org.jetbrains.kotlin.multiplatform")
}

val publications: PublicationContainer = (extensions.getByName("publishing") as PublishingExtension).publications

signing {
  useGpgCmd()
  if (Ci.isRelease)
    sign(publications)
}
