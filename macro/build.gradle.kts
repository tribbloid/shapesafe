val vs: Versions = versions()

dependencies {

    api(project(":graph-commons"))
    testApi(project(":macro:testlib"))

//    api("eu.timepit:refined_${vv.scalaBinaryV}:0.9.14")
//    /TODO: remove, most arity inspection macros doesn't work on collection/tuple, using shapeless Length as cheap alternative
}