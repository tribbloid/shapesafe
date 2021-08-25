import org.gradle.api.Project

class Versions(self: Project) {

    // TODO : how to group them?
    val projectGroup = "org.shapesafe"
    val projectRootID = "shapesafe"

    val projectV = "0.1.0-SNAPSHOT"
    val projectVMajor = projectV.removeSuffix("-SNAPSHOT")
//    val projectVComposition = projectV.split('-')

    val scalaGroup: String = self.properties.get("scalaGroup").toString()

    val scalaV: String = self.properties.get("scalaVersion").toString()

    protected val scalaVParts = scalaV.split('.')

    val scalaBinaryV: String = scalaVParts.subList(0, 2).joinToString(".")
    val scalaMinorV: String = scalaVParts[2]

    val shapelessV: String = "2.3.3"

    val sparkV: String = self.properties.get("sparkVersion").toString()

    val scalatestV: String = "3.2.3"

    val splainV: String = self.properties.get("splainVersion")?.toString() ?: ""
}
