import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
  mavenCentral()
  maven {
    url = uri("https://oss.sonatype.org/content/repositories/snapshots")
  }
}

plugins {
  java
  kotlin("multiplatform").version("1.6.0") apply false
  `java-library`
  id("maven-publish")
  signing
  id("org.jetbrains.dokka") version "1.6.10"
  id("io.kotest.multiplatform") version "5.0.3"
  id("ru.vyarus.animalsniffer") version "1.5.4"
}

allprojects {
  group = "io.kotest.extensions"
  version = Ci.version

  repositories {
    mavenCentral()
    maven {
      url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
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
