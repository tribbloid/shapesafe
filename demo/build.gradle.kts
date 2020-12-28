val vs: Versions = versions()

dependencies {

    implementation(project(":core:core-breeze"))
    testApi(testFixtures(project(":macro")))
}
