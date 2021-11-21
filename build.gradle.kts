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
    `java-test-fixtures`

    scala
    kotlin("jvm") version "1.5.31" // TODO: remove?

    idea

    `maven-publish`

    id("com.github.ben-manes.versions" ) version "0.39.0"
}

val rootID = vs.projectRootID

allprojects {

    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "java-test-fixtures")

    // apply(plugin = "bloop")
    // DO NOT enable! In VSCode it will cause the conflict:
    // Cannot add extension with name 'bloop', as there is an extension already registered with that name

    apply(plugin = "scala")
    apply(plugin = "kotlin")

    apply(plugin = "idea")

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

    // resolving jar hells
    configurations.all {
        resolutionStrategy.dependencySubstitution {
            // TODO: use `constraints` as below
            substitute(
                module("com.chuusai:shapeless_${vs.scalaBinaryV}")
            ).apply {
                with(module("com.chuusai:shapeless_${vs.scalaBinaryV}:${vs.shapelessV}"))
            }
        }
    }

    dependencies {

        // see https://github.com/gradle/gradle/issues/13067
        fun bothImpl(constraintNotation: Any) {
            implementation(constraintNotation)
            testFixturesImplementation(constraintNotation)
        }

        constraints {

            bothImpl("${vs.scalaGroup}:scala-compiler:${vs.scalaV}")
            bothImpl("${vs.scalaGroup}:scala-library:${vs.scalaV}")
            bothImpl("${vs.scalaGroup}:scala-reflect:${vs.scalaV}")
        }

        //https://github.com/tek/splain
        if (vs.splainV != "") {
            logger.warn("Using Splain " + vs.splainV)

            scalaCompilerPlugins("io.tryp:splain_${vs.scalaV}:${vs.splainV}")
        }

//        compileOnly(kotlin("stdlib"))
//        compileOnly(kotlin("stdlib-jdk8"))

        api("eu.timepit:singleton-ops_${vs.scalaBinaryV}:0.5.2") // used by all modules

        testImplementation("org.scalatest:scalatest_${vs.scalaBinaryV}:${vs.scalaTestV}")
        testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

        // TODO: alpha project, switch to mature solution once https://github.com/scalatest/scalatest/issues/1454 is solved
        testRuntimeOnly("co.helmethair:scalatest-junit-runner:0.1.10")

//        testRuntimeOnly("com.vladsch.flexmark:flexmark-all:0.35.10")

    }

    task("dependencyTree") {

        dependsOn("dependencies")
    }

    tasks {
        val jvmTarget = JavaVersion.VERSION_1_8.toString()

        withType<ScalaCompile> {

            targetCompatibility = jvmTarget

            scalaCompileOptions.apply {

//                    isForce = true

                loggingLevel = "verbose"

                val compilerOptions =

                    mutableListOf(

                        "-encoding", "UTF-8",
                        "-unchecked",
                        "-deprecation",
                        "-feature",

                        "-language:higherKinds",
//                            "-Xfatal-warnings",

                        "-Xlint:poly-implicit-overload",
                        "-Xlint:option-implicit",
                        "-Wunused:imports",

//                        "-Ydebug",
//                        "-Yissue-debug"
//                    ,
//                    "-Ytyper-debug",
//                    "-Vtyper"

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
                            "-Vimplicits-verbose-tree"
//                            "-Vtype-diffs"
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

//        kotlin {}
// TODO: remove, kotlin is not in scope at the moment
//
//        withType<KotlinCompile> {
//
//
//            kotlinOptions.jvmTarget = jvmTarget
////            kotlinOptions.freeCompilerArgs += "-XXLanguage:+NewInference"
//            // TODO: re-enable after kotlin compiler argument being declared safe
//        }

        test {

            minHeapSize = "1024m"
            maxHeapSize = "4096m"

            testLogging {
//                events = setOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED, org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED, org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED, org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT)
//                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
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
//    scala {
//        this.zincVersion
//    }

}

subprojects {

    publishing {
        val moduleID =
            if (project.name.startsWith(rootID)) project.name
            else rootID + "-" + project.name

        publications {
            create<MavenPublication>("maven") {
                groupId = groupId
                artifactId = moduleID
                version = version

                val javaComponent = components["java"] as AdhocComponentWithVariants
                from(javaComponent)

                javaComponent.withVariantsFromConfiguration(configurations["testFixturesApiElements"]) { skip() }
                javaComponent.withVariantsFromConfiguration(configurations["testFixturesRuntimeElements"]) { skip() }
            }
        }
    }
}

idea {

    targetVersion = "2020"

    module {

        excludeDirs = excludeDirs + listOf(
            file(".gradle"),
            file(".github"),

            file ("target"),
//                        file ("out"),

            file(".idea"),
            file(".vscode"),
            file(".bloop"),
            file(".bsp"),
            file(".metals"),
            file(".ammonite"),

            file("logs"),

            // apache spark
            file("warehouse"),

            file("spike"),

            file("splain")
        )

        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
