val vs: Versions = versions()

dependencies {

    api(project(":core"))
    testApi(testFixtures(project(":core")))

    // https://mvnrepository.com/artifact/org.scalanlp/breeze
    api("org.scalanlp:breeze_${vs.scalaBinaryV}:1.1")
    // https://mvnrepository.com/artifact/org.freehep/jas-core
    // TODO: add this library to allow visualization of expressions
//    api("org.freehep:jas-core:3.2.0")
}