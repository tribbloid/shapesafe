val vs: Versions = versions()

dependencies {

    api(project(":verify"))
    testApi(testFixtures(project(":verify")))

    // https://mvnrepository.com/artifact/org.scalanlp/breeze
    api("org.scalanlp:breeze_${vs.scalaBinaryV}:1.2")
    // https://mvnrepository.com/artifact/org.freehep/jas-core
    // TODO: add this library to allow visualization of expressions
//    api("org.freehep:jas-core:3.2.0")
}