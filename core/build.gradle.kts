val vs: Versions = versions()

dependencies {

    api(project(":macro2"))
    testFixturesApi(testFixtures(project(":macro2")))
}