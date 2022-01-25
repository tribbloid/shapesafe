val vs = versions()

buildscript {
    repositories {
        // Add here whatever repositories you're already using
        mavenCentral()
    }

//    val vs = versions()

    dependencies {
        classpath("ch.epfl.scala:gradle-bloop_2.12:1.4.11") // suffix is always 2.12, weird
    }
}

plugins {
//    base
    java
    `java-library`
    `java-test-fixtures`

    scala
//    kotlin("jvm") version "1.6.10" // TODO: remove?

    idea

    signing
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"

    id("com.github.ben-manes.versions") version "0.41.0"
}

val sonatypeApiUser = providers.gradleProperty("sonatypeApiUser")
val sonatypeApiKey = providers.gradleProperty("sonatypeApiKey")
if (sonatypeApiUser.isPresent && sonatypeApiKey.isPresent) {
    nexusPublishing {
        repositories {
            sonatype {

                nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
                snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

                username.set(sonatypeApiUser)
                password.set(sonatypeApiKey)
                useStaging.set(true)
            }
        }
    }
} else {
    logger.warn("Sonatype API credential not defined, skipping ...")
}

allprojects {

    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "java-test-fixtures")

    // apply(plugin = "bloop")
    // DO NOT enable! In VSCode it will cause the conflict:
    // Cannot add extension with name 'bloop', as there is an extension already registered with that name

    apply(plugin = "scala")
//    apply(plugin = "kotlin")

    apply(plugin = "idea")

    apply(plugin = "signing")
    apply(plugin = "maven-publish")

    group = vs.projectGroup
    version = vs.projectV

    repositories {
        mavenLocal()
        mavenCentral()
//        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
        maven("https://scala-ci.typesafe.com/artifactory/scala-integration/") // scala SNAPSHOT
    }

    task("dependencyTree") {

        dependsOn("dependencies")
    }

    tasks {
        val jvmTarget = JavaVersion.VERSION_1_8.toString()

        withType<ScalaCompile> {

            targetCompatibility = jvmTarget

            scalaCompileOptions.apply {

                loggingLevel = "verbose"

                val compilerOptions =

                    mutableListOf(

                        "-encoding", "UTF-8",
                        "-unchecked",
                        "-deprecation",
                        "-feature",

                        "-language:higherKinds",

                        "-Xlint:poly-implicit-overload",
                        "-Xlint:option-implicit",
                        "-Wunused:imports",

                        "-g:line",

//                        "-Ylog",
//                        "-Ydebug",
                        "-Vissue",
                        "-Yissue-debug"

//                    ,
//                    "-Xlog-implicits",
//                    "-Xlog-implicit-conversions",
//                    "-Xlint:implicit-not-found",
//                    "-Xlint:implicit-recursion"
                    )

                if (vs.splainV != "") {
                    compilerOptions.addAll(
                        listOf(
                            "-Vimplicits",
                            "-Vimplicits-verbose-tree",
                            "-Vtype-diffs"
                        )
                    )
                }

                additionalParameters = compilerOptions

                forkOptions.apply {

                    memoryInitialSize = "1g"
                    memoryMaximumSize = "4g"

                    // this may be over the top but the test code in macro & core frequently run implicit search on church encoded Nat type
                    jvmArgs = listOf(
                        "-Xss256m"
                    )
                }
            }
        }

        test {

            minHeapSize = "1024m"
            maxHeapSize = "4096m"

            testLogging {
                showExceptions = true
                showCauses = true
                showStackTraces = true

                // stdout is used for occasional manual verification
                showStandardStreams = true
            }

            useJUnitPlatform {
                includeEngines("scalatest")
                testLogging {
                    events("passed", "skipped", "failed")
                }
            }

        }
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    idea {

        module {

            excludeDirs = excludeDirs + files(

                "target",
                "out",

                ".idea",
                ".vscode",
                ".bloop",
                ".bsp",
                ".metals",
                "bin",

                ".ammonite",

                "logs",

            )
        }
    }
}

subprojects {

    // resolving version conflicts
    // TODO: remove, already defined in `constraints` as below
//    configurations.all {
//        resolutionStrategy.dependencySubstitution {
//            substitute(
//                module("com.chuusai:shapeless_${vs.scalaBinaryV}")
//            ).apply {
//                using(module("com.chuusai:shapeless_${vs.scalaBinaryV}:${vs.shapelessV}"))
//            }
//        }
//    }

    dependencies {

        // see https://github.com/gradle/gradle/issues/13067
        fun bothImpl(constraintNotation: Any) {
            implementation(constraintNotation)
            testFixturesImplementation(constraintNotation)
        }

        constraints {
            bothImpl("com.chuusai:shapeless_${vs.scalaBinaryV}:${vs.shapelessV}")
        }

        bothImpl("${vs.scalaGroup}:scala-compiler:${vs.scalaV}")
        bothImpl("${vs.scalaGroup}:scala-library:${vs.scalaV}")
        bothImpl("${vs.scalaGroup}:scala-reflect:${vs.scalaV}")

        testImplementation("org.scalatest:scalatest_${vs.scalaBinaryV}:${vs.scalaTestV}")
        testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

        // TODO: alpha project, switch to mature solution once https://github.com/scalatest/scalatest/issues/1454 is solved
        testRuntimeOnly("co.helmethair:scalatest-junit-runner:0.1.10")

//        testRuntimeOnly("com.vladsch.flexmark:flexmark-all:0.35.10")
        //https://github.com/tek/splain
        if (vs.splainV != "") {
            val splainD = "io.tryp:splain_${vs.scalaV}:${vs.splainV}"
            logger.warn("Using " + splainD)

            scalaCompilerPlugins(splainD)
        }
    }

    // https://stackoverflow.com/a/66352905/1772342
    val signingSecretKey = providers.gradleProperty("signing.gnupg.secretKey")
    val signingKeyPassphrase = providers.gradleProperty("signing.gnupg.passphrase")
    signing {
        useGpgCmd()
        if (signingSecretKey.isPresent) {
            useInMemoryPgpKeys(signingSecretKey.get(), signingKeyPassphrase.get())
//            useInMemoryPgpKeys(signingKeyID.get(), signingSecretKey.get(), signingKeyPassphrase.get())
            sign(extensions.getByType<PublishingExtension>().publications)
        } else {
            logger.warn("PGP signing key not defined, skipping ...")
        }
    }

    publishing {
        val suffix = "_" + vs.scalaBinaryV

        val rootID = vs.projectRootID

        val moduleID =
            if (project.name.equals(rootID))
                throw UnsupportedOperationException("root project should not be published")
            else rootID + "-" + project.name + suffix

        val whitelist = setOf("graph-commons", "macro", "core")

        if (whitelist.contains(project.name)) {

            publications {
                create<MavenPublication>("maven") {
                    artifactId = moduleID

                    val javaComponent = components["java"] as AdhocComponentWithVariants
                    from(javaComponent)

                    javaComponent.withVariantsFromConfiguration(configurations["testFixturesApiElements"]) { skip() }
                    javaComponent.withVariantsFromConfiguration(configurations["testFixturesRuntimeElements"]) { skip() }


                    pom {
                        licenses {
                            license {
                                name.set("Apache License, Version 2.0")
                                url.set("https://www.apache.org/licenses/LICENSE-2.0")
                            }
                        }

                        name.set("shapesafe")
                        description.set("a static typechecker for (named) tensor operations in scala")

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
    }
}

idea {

    module {

        excludeDirs = excludeDirs + files(
            ".gradle",

            // apache spark
            "warehouse",

            "spike",
            "splain",
            "graph-commons"
        )

        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

//idea {
//
//    module {
//
//        excludeDirs = excludeDirs + files(
//
//            // submodules
////            "graph-commons",
//            "splain"
//        )
//    }
//}
