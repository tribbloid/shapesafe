package org.shapesafe.m.viz

import com.tribbloids.graph.commons.util.reflect.format.{EnableOvrd, Formats}
import com.tribbloids.graph.commons.util.viz.TypeVizFormat

import scala.language.experimental.macros

case object KindVizCT extends VizCTSystem {

  override def vizFormat: TypeVizFormat = Formats.KindName.Short
  override def useTree: Boolean = true

  implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

  case object NoTree extends Updated {

    override def useTree: Boolean = false

    implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }

  case object Ovrd extends Updated {

    override def vizFormat: TypeVizFormat = EnableOvrd(outer.typeFormat)
    override def useTree: Boolean = false

    implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
  }
}
