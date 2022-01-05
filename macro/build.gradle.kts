val vs: Versions = versions()

dependencies {

    api(project(":graph-commons"))
    testFixturesApi(testFixtures(project(":graph-commons")))
}