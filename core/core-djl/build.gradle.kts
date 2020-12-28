val vs: Versions = versions()

dependencies {

    api(project(":core"))
    testApi(testFixtures(project(":core")))

    // TODO: the following are too specialised and should only be used in testing
    api("ai.djl.pytorch:pytorch-engine:0.9.0")
    api("ai.djl.pytorch:pytorch-native-auto:1.7.0")
}