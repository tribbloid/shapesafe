package org.shapesafe.m.viz

import org.shapesafe.graph.commons.util.reflect.format.TypeFormat
import org.shapesafe.graph.commons.util.viz.TypeVizFormat

import scala.language.experimental.macros

case object TypeVizCT extends VizCTSystem {

  override lazy val format: TypeVizFormat =
    TypeFormat.Default

  override def useTree: Boolean = true

  implicit def infoOf[I]: Info[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

//  case object Stub extends VizCTSystem {
//
//    override def vizFormat: TypeVizFormat = Formats.TypeInfo.Short
//    override def useTree: Boolean = false
//
//    implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
//  }
}
