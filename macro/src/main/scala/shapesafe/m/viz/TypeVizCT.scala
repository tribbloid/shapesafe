package shapesafe.m.viz

import ai.acyclic.prover.commons.reflect.format.{Formats0, TypeFormat}
import ai.acyclic.prover.commons.viz.TypeVizFormat

import scala.language.experimental.macros

case object TypeVizCT extends VizCTSystem {

  override lazy val format: TypeVizFormat =
    TypeFormat.Default

  override def useTree: Boolean = true

  implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

  case object Short extends SubSystem {

    override lazy val format: TypeVizFormat = Formats0.TypeInfo.HidePackage.recursively.DeAlias

    implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }
}
