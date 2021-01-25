import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

plugins {
//    base
    java

    scala
    kotlin("jvm") version "1.3.72" // TODO: remove?

    idea

    `maven-publish`

    id("com.github.ben-manes.versions" ) version "0.36.0"
}

allprojects {

    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "java-test-fixtures")

    apply(plugin = "scala")
    apply(plugin = "kotlin")

    apply(plugin = "idea")

    apply(plugin = "maven-publish")

    val vs = this.versions()

    group = "edu.umontreal.shapesafe"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }

    // resolving jar hells
    configurations.all {
        resolutionStrategy.dependencySubstitution {
            substitute(module("com.chuusai:shapeless_${vs.scalaBinaryV}")).apply {
                with(module("com.chuusai:shapeless_${vs.scalaBinaryV}:2.3.3"))
            }
        }
    }

    dependencies {

        testImplementation("${vs.scalaGroup}:scala-compiler:${vs.scalaV}")
        testImplementation("${vs.scalaGroup}:scala-library:${vs.scalaV}")
        testImplementation("${vs.scalaGroup}:scala-reflect:${vs.scalaV}")

        //https://github.com/tek/splain
//        scalaCompilerPlugins("io.tryp:splain_${vs.scalaV}:0.5.7")
        //TODO: incompatible with testFixtures?

        testImplementation(kotlin("stdlib"))
        testImplementation(kotlin("stdlib-jdk8"))

        api("eu.timepit:singleton-ops_${vs.scalaBinaryV}:0.5.2") // used by all modules

//        api("eu.timepit:singleton-ops_${vs.scalaBinaryV}:0.5.0+22-59783019+20200731-1305-SNAPSHOT")

        testImplementation("org.scalatest:scalatest_${vs.scalaBinaryV}:${vs.scalatestV}")
        testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")

        // TODO: alpha project, switch to mature solution once https://github.com/scalatest/scalatest/issues/1454 is solved
        testRuntimeOnly("co.helmethair:scalatest-junit-runner:0.1.8")

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

                additionalParameters = listOf(
                    "-encoding", "utf8",
                    "-unchecked",
                    "-deprecation",
                    "-feature",
//                            "-Xfatal-warnings",

                    "-Xlint:poly-implicit-overload",
                    "-Xlint:option-implicit",

                    "-Xlog-implicits",
                    "-Xlog-implicit-conversions",

                    "-Yissue-debug"
//                        ,
//                        "-Ytyper-debug"
//                        "-Vtyper"

                    // the following only works on scala 2.13
//                        ,
//                        "-Xlint:implicit-not-found",
//                        "-Xlint:implicit-recursion"
                )

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

            useJUnitPlatform {
                includeEngines("scalatest")
                testLogging {
                    events("passed", "skipped", "failed")
                }
            }

            testLogging {
//                events = setOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED, org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED, org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED, org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT)
//                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true

                // stdout is used for occasional manual verification
                showStandardStreams = true
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


    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = vs.projectGroup
                artifactId = "shapesafe-" + project.name
                version = vs.projectV

                from(components["java"])

                suppressPomMetadataWarningsFor("testFixturesApiElements")
                suppressPomMetadataWarningsFor("testFixturesRuntimeElements")
            }
        }
    }
}

idea {

    targetVersion = "2020"


    module {

        excludeDirs.addAll(
            listOf(
                file(".gradle"),
                file(".build"),
                file(".idea"),
                file(".github"),

                file("logs"),

                // apache spark
                file("warehouse")
            )
        )

        isDownloadJavadoc = true
        isDownloadSources = true
    }
}