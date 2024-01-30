val vs: Versions = versions()

dependencies {

    api(project(":verify"))
    testImplementation(testFixtures(project(":verify")))

    // TODO: the following are too specialised and should only be used in testing
    api("ai.djl.pytorch:pytorch-engine:0.26.0")
    api("ai.djl.pytorch:pytorch-native-auto:1.9.1")
}