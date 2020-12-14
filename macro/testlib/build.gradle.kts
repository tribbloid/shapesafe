val vs: Versions = versions()

dependencies {

    implementation("org.scalatest:scalatest_${vs.scalaBinaryV}:3.0.8")

    api(project(":graph-commons"))
//    api(project(":graph-commons:testlib"))

//    api("eu.timepit:refined_${vv.scalaBinaryV}:0.9.14")
//    /TODO: remove, most arity inspection macros doesn't work on collection/tuple, using shapeless Length as cheap alternative
}