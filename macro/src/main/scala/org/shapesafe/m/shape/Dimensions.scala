package org.shapesafe.m.shape

import org.shapesafe.m.arity.Expression
import org.shapesafe.m.tuple.{CanInfix_><, StaticTuples}

import scala.language.implicitConversions

object Dimensions extends StaticTuples.Total[Expression] with CanInfix_>< {}
