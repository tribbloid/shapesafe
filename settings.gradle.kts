//val versions = gradle.rootProject.versions()


include(
        "common",
        "demo"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
