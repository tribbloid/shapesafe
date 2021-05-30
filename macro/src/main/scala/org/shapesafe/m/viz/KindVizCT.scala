package org.shapesafe.m.viz

import org.shapesafe.graph.commons.util.reflect.format.{EnableOvrd, Formats}
import org.shapesafe.graph.commons.util.viz.TypeVizFormat

import scala.language.experimental.macros

case object KindVizCT extends VizCTSystem {

  override def format: TypeVizFormat = Formats.KindName.HidePackage.recursively
  override def useTree: Boolean = true

  implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

  case object NoTree extends Updated {

    override def useTree: Boolean = false

    implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }

  case object Ovrd extends Updated {

    override def format: TypeVizFormat = EnableOvrd(outer.typeFormat)
    override def useTree: Boolean = false

    implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }
}
