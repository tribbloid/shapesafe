val vs: Versions = versions()

dependencies {

    api("eu.timepit:singleton-ops_${vs.scalaBinaryV}:0.5.0")
    testApi(project(":macro:testcommon"))

//    api("eu.timepit:refined_${vv.scalaBinaryV}:0.9.14")
//    /TODO: remove, most arity inspection macros doesn't work on collection/tuple, using shapeless Length as cheap alternative
}