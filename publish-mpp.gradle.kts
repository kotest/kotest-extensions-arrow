apply(from = "$rootDir/signing-pom-details.gradle.kts")


val javadoc by tasks.creating(Jar::class) {
  group = "build"
  description = "Assembles Javadoc jar file from for publishing"
  archiveClassifier.set("javadoc")
}

val javadocJar by tasks.creating(Jar::class) {
  group = JavaBasePlugin.DOCUMENTATION_GROUP
  description = "Assembles java doc to jar"
  archiveClassifier.set("javadoc")
  from(javadoc)
}

fun Project.publishing(action: PublishingExtension.() -> Unit) =
  configure(action)

val publications: PublicationContainer = (extensions.getByName("publishing") as PublishingExtension).publications

publishing {
  publications.withType<MavenPublication>().forEach {
    it.apply {
      //if (Ci.isRelease)
      artifact(javadocJar)
    }
  }
}

//region Fix Gradle error Reason: Task <publish> uses this output of task <sign> without declaring an explicit or implicit dependency.
// https://github.com/gradle/gradle/issues/26091
tasks.withType<AbstractPublishToMaven>().configureEach {
  val signingTasks = tasks.withType<Sign>()
  mustRunAfter(signingTasks)
}
