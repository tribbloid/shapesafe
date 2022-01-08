val vs: Versions = versions()

dependencies {

    api(project(":graph-commons"))
    testFixturesApi(testFixtures(project(":graph-commons")))

    api("eu.timepit:singleton-ops_${vs.scalaBinaryV}:0.5.2")
}