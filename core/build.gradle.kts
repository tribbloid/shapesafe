val vs: Versions = versions()

dependencies {

    api(project(":macro"))
    testFixturesApi(testFixtures(project(":macro")))
}