import org.gradle.api.Project

class Versions(self: Project) {

    // TODO : how to group them?
    val projectGroup = "ai.acyclic.shapesafe"
    val projectRootID = "shapesafe"

    val projectV = "0.1.2"
    val projectVMajor = projectV.removeSuffix("-SNAPSHOT")
//    val projectVComposition = projectV.split('-')

    val scalaGroup: String = self.properties.get("scalaGroup").toString()

    val scalaV: String = self.properties.get("scalaVersion").toString()

    protected val scalaVParts = scalaV.split('.')

    val scalaBinaryV: String = scalaVParts.subList(0, 2).joinToString(".")
    val scalaMinorV: String = scalaVParts[2]

    val shapelessV: String = "2.3.7"

    val sparkV: String = self.properties.get("sparkVersion").toString()

    val scalaTestV: String = "3.2.3"

    val splainV: String = self.properties.get("splainVersion")?.toString() ?: ""
}
