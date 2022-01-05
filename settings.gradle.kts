
include("graph-commons")
project(":graph-commons").projectDir = file("graph-commons/core")

include(
    // should be skipped on CI, contains local experiments only
    ":macro",
    // uses unstable & experimental scala features, should be modified very slowly & carefully
    ":core",
    ":verify",
    ":verify:breeze",
    ":verify:djl",
//    // uses common scala features
    ":shapesafe-demo"
//    "spike"
)


pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
