buildscript {
    repositories {
        // Add here whatever repositories you're already using
        mavenCentral()
    }

    dependencies {
        classpath("ch.epfl.scala:gradle-bloop_2.12:1.6.2") // suffix is always 2.12, weird
    }
}

plugins {
    id("ai.acyclic.scala2-conventions")
    id("ai.acyclic.publish-conventions")
}

val vs = versions()
val shapelessV = "2.3.10"

nexusPublishing {
    repositories {
        sonatype {

            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}


allprojects {

    dependencies {

        constraints {
            implementation("com.chuusai:shapeless_${vs.scala.binaryV}:${shapelessV}")
        }
    }
}

subprojects {

    publishing {

        val whitelist = setOf(":prover-commons:core", ":prover-commons:meta2", ":macro", ":core")

        val _path = project.path
        if (whitelist.contains(_path)) {

            publications {

                create<MavenPublication>("maven") {

                    pom {
                        licenses {
                            license {
                                name.set("Apache License, Version 2.0")
                                url.set("https://www.apache.org/licenses/LICENSE-2.0")
                            }
                        }

                        name.set("shapesafe")
                        description.set(
                            "SHAPE/S∀F∃: static prover/type-checker for N-D array programming in Scala," + " a use case of intuitionistic type theory"
                        )

                        val github = "https://github.com/tribbloid"
                        val repo = github + "/shapesafe"

                        url.set(repo)

                        developers {
                            developer {
                                id.set("tribbloid")
                                name.set("Peng Cheng")
                                url.set(github)
                            }
                        }
                        scm {
                            connection.set("scm:git@github.com:tribbloid/shapesafe")
                            url.set(repo)
                        }
                    }
                }
            }
        }
        else {
            logger.warn("`${_path}` is not intended to be published")
        }
    }
}

idea {

    module {

        excludeDirs = excludeDirs + files(
            // apache spark
            "warehouse",

            "splain", "prover-commons"
        )
    }
}
