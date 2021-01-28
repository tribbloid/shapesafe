import org.gradle.api.Project

class Versions(self: Project) {

    val projectGroup = "com.tribbloids.shapesafe"

    val projectV = self.properties.get("projectVersion").toString()

    val projectVComposition = projectV.split('-')

    val scalaGroup: String = self.properties.get("scalaGroup").toString()

    val scalaV: String = self.properties.get("scalaVersion").toString()

    protected val scalaVParts = scalaV.split('.')

    val scalaBinaryV: String = scalaVParts.subList(0, 2).joinToString(".")
    val scalaMinorV: String = scalaVParts[2]

    val shapelessV: String = "2.3.3"

    val sparkV: String = self.properties.get("sparkVersion").toString()

    val scalatestV: String = "3.2.3"
}
