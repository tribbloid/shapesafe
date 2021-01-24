//val versions = gradle.rootProject.versions()


include(
    // should be skipped on CI, contains local experiments only
    ":graph-commons",
    ":macro",
    // uses unstable & experimental scala features, should be modified very slowly & carefully.
    // also, despite its name it actually doesn't contain any macro, but it is an option in the future
    ":core",
    ":core:core-breeze",
    ":core:core-djl",
    // uses common scala features
    ":shapesafe-demo"
//    "spike"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
