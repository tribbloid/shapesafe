val vs: Versions = versions()

dependencies {

    api(project(":macro"))
    testFixturesApi(testFixtures(project(":macro")))

    // https://mvnrepository.com/artifact/org.freehep/jas-core
    // TODO: add this library to allow visualization of expressions
//    api("org.freehep:jas-core:3.2.0")
}