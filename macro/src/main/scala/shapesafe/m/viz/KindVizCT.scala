package shapesafe.m.viz

import ai.acyclic.prover.commons.meta2.format.Formats0.KindName
import ai.acyclic.prover.commons.meta2.format.{EnableOvrd, TypeFormat}

import scala.language.experimental.macros

case object KindVizCT extends VizCTSystem {

  override lazy val typeFormat: TypeFormat =
    KindName.HidePackage.recursively

  override def useTree: Boolean = true

  implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

  case object NoTree extends SubSystem {

    override def useTree: Boolean = false

    implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }

  case object WithOvrd extends SubSystem {

    override lazy val typeFormat: TypeFormat = EnableOvrd(outer.typeFormat)
    override def useTree: Boolean = false

    implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }
}
