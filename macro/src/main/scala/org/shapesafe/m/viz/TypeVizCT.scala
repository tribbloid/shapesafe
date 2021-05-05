package org.shapesafe.m.viz

import com.tribbloids.graph.commons.util.reflect.format.{Formats, TypeFormat}
import com.tribbloids.graph.commons.util.viz.TypeVizFormat

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

case object TypeVizCT extends VizCTSystem {

  override val vizFormat: TypeVizFormat = TypeFormat.Default
  override def useTree: Boolean = true

  implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]

//  case object Stub extends VizCTSystem {
//
//    override def vizFormat: TypeVizFormat = Formats.TypeInfo.Short
//    override def useTree: Boolean = false
//
//    implicit def infoOf[I]: InfoOf[I] = macro VizCTSystem.Macros.infoOf[I, this.type]
//  }
}
