val vs: Versions = versions()

dependencies {

    api(project(":macro"))
    testApi(project(":macro:testcommon"))

    // https://mvnrepository.com/artifact/org.scalanlp/breeze
    api("org.scalanlp:breeze_${vs.scalaBinaryV}:1.0")



}