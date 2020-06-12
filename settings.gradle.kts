//val versions = gradle.rootProject.versions()


include(
        "spike", // should be skipped on CI, contains local experiments only
        "common",
        "demo"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
