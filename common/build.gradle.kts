val vs: Versions = versions()

dependencies {

    implementation("eu.timepit:singleton-ops_${vs.scalaBinaryV}:0.5.0")

//    implementation("eu.timepit:refined_${vv.scalaBinaryV}:0.9.14")
//    /TODO: remove, most arity inspection macros doesn't work on collection/tuple, using shapeless Length as cheap alternative
}