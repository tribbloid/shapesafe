package org.shapesafe.core.shape

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.tuple.{CanInfix_><, StaticTuples}

import scala.language.implicitConversions

// should it be "Arities"?
object Dimensions extends StaticTuples.Total[Arity] with CanInfix_>< {}
