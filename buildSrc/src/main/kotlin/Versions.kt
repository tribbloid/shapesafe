import org.gradle.api.Project

class Versions(private val self: Project) {

    // TODO : how to group them?
    val projectGroup = "ai.acyclic.shapesafe"
    val projectRootID = "shapesafe"

    val projectV = "0.2.0-SNAPSHOT"
    val projectVMajor = projectV.removeSuffix("-SNAPSHOT")
//    val projectVComposition = projectV.split('-')

    inner class Scala {
        val group: String = self.properties["scala.group"].toString()

        val v: String = self.properties["scala.version"].toString()
        protected val vParts: List<String> = v.split('.')

        val binaryV: String = vParts.subList(0, 2).joinToString(".")
        val minorV: String = vParts[2]
    }
    val scala = Scala()

    val shapelessV: String = "2.3.7"

    val scalaTestV: String = "3.2.12"

    val splainV: String = self.properties.get("splain.version")?.toString() ?: ""
}
