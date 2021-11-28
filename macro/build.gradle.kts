val vs: Versions = versions()

dependencies {

    api(project(":graph-commons-core"))
    testFixturesApi(testFixtures(project(":graph-commons-core")))
}