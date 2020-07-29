import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    java
    scala
    kotlin("jvm") version "1.3.72"
    idea
}

allprojects {

    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "scala")
    apply(plugin = "kotlin")
    apply(plugin = "idea")

    val vs = this.versions()

    group = "edu.umontreal.shapesafe"
    version = "0.0.1-SNAPSHOT"

    repositories {
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

        implementation("org.scala-lang:scala-compiler:${vs.scalaV}")
        implementation("org.scala-lang:scala-library:${vs.scalaV}")
        implementation("org.scala-lang:scala-reflect:${vs.scalaV}")

        //https://github.com/tek/splain
        scalaCompilerPlugins("io.tryp:splain_${vs.scalaV}:0.5.7")

        implementation(kotlin("stdlib"))
        implementation(kotlin("stdlib-jdk8"))

        testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")

        // TODO: alpha project, switch to mature solution once https://github.com/scalatest/scalatest/issues/1454 is solved
        testRuntimeOnly("co.helmethair:scalatest-junit-runner:0.1.3")
        testImplementation("org.scalatest:scalatest_${vs.scalaBinaryV}:3.0.8")

    }

    task("dependencyTree") {

        dependsOn("dependencies")
    }

    tasks {
        val jvmTarget = JavaVersion.VERSION_1_8.toString()

//        scala {
//            this.zincVersion
//        }

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
                        "-Xlog-implicit-conversions"

                        ,
                        "-Yissue-debug"

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

        withType<KotlinCompile> {


            kotlinOptions.jvmTarget = jvmTarget
//            kotlinOptions.freeCompilerArgs += "-XXLanguage:+NewInference"
            // TODO: re-enable after kotlin compiler argument being declared safe
        }

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
            }
        }
    }

    idea {

        targetVersion = "2020"


        module {

            // apache spark
            excludeDirs.add(file("warehouse"))
            excludeDirs.add(file("latex"))

            // gradle log
            excludeDirs.add(file("logs"))
            excludeDirs.add(file("gradle"))

            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }
}


