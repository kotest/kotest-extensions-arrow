import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
   java
   `java-library`
   id("java-library")
   id("maven-publish")
   signing
   `maven-publish`
   kotlin("jvm").version(Libs.kotlinVersion)
}

allprojects {
   apply(plugin = "org.jetbrains.kotlin.jvm")

   group = Libs.org
   version = Ci.version

   dependencies {
      implementation(Libs.Kotest.assertionsShared)
      implementation(Libs.Kotest.assertionsCore)
      implementation(Libs.Kotest.property)
      compileOnly(Libs.Arrow.core)
      testImplementation(Libs.Kotest.junit5)
      testImplementation(Libs.Arrow.core)
   }

   tasks.named<Test>("test") {
      useJUnitPlatform()
      testLogging {
         showExceptions = true
         showStandardStreams = true
         exceptionFormat = TestExceptionFormat.FULL
      }
   }

   tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      kotlinOptions {
         jvmTarget = JavaVersion.VERSION_1_8.majorVersion
         freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
      }
   }

   repositories {
      mavenCentral()
      maven {
         url = uri("https://oss.sonatype.org/content/repositories/snapshots")
      }
   }
}

apply("./publish.gradle.kts")
