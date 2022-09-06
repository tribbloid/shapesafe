val vs: Versions = versions()

dependencies {

    api(project(":verify"))
    testImplementation(testFixtures(project(":verify")))

    // https://mvnrepository.com/artifact/org.scalanlp/breeze
    api("org.scalanlp:breeze_${vs.scalaBinaryV}:2.1.0")
    // https://mvnrepository.com/artifact/org.freehep/jas-core
    // TODO: add this library to allow visualization of expressions
//    api("org.freehep:jas-core:3.2.0")
}