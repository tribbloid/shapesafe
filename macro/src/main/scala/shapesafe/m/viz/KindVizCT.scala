package shapesafe.m.viz

import ai.acyclic.graph.commons.reflect.format.EnableOvrd
import ai.acyclic.graph.commons.reflect.format.Formats0.KindName
import ai.acyclic.graph.commons.viz.TypeVizFormat

import scala.language.experimental.macros

case object KindVizCT extends VizCTSystem {

  override lazy val format: TypeVizFormat =
    KindName.HidePackage.recursively

  override def useTree: Boolean = true

  implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

  case object NoTree extends SubSystem {

    override def useTree: Boolean = false

    implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }

  case object WithOvrd extends SubSystem {

    override lazy val format: TypeVizFormat = EnableOvrd(outer.typeFormat)
    override def useTree: Boolean = false

    implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }
}
