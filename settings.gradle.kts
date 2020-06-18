//val versions = gradle.rootProject.versions()


include(
        "spike", // should be skipped on CI, contains local experiments only
        "macro" // uses unstable & experimental scala features, should change very slowly
//        "core", // uses common scala features
//        "demo"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
