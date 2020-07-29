val vv: Versions = versions()

dependencies {

    implementation(project(":core"))
    testApi(testFixtures(project(":macro")))
}
