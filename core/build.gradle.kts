val vs: Versions = versions()

dependencies {

    testFixturesApi(testFixtures(project(":graph-commons-core")))

    api(project(":macro"))

//    api("eu.timepit:refined_${vv.scalaBinaryV}:0.9.14")
//    /TODO: remove, most arity inspection macros doesn't work on collection/tuple, using shapeless Length as cheap alternative
}