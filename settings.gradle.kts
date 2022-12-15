
val noVerify: String? by settings

val noSpike: String? by settings

fun isEnabled(profile: String?): Boolean {
    val result = profile.toBoolean() || profile == ""
    return result
}

include("prover-commons")
project(":prover-commons").projectDir = file("prover-commons/core")

include(
    // should be skipped on CI, contains local experiments only
    ":macro",
    // uses unstable & experimental scala features, should be modified very slowly & carefully
    ":core",
    "shapesafe-demo"
//    // uses common scala features
)

if (!isEnabled(noVerify)) {

    include(
        ":verify",
        ":verify:breeze",
        ":verify:djl"
    )
}

if (!isEnabled(noSpike)) {

    include(
        "spike"
    )
}

pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-dev")
}
