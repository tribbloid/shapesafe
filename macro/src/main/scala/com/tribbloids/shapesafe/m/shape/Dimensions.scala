package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.tuple.{CanCrossAlways, CanInfix, TupleSystem}

import scala.language.implicitConversions

object Dimensions extends TupleSystem[Expression] with CanInfix[Expression] with CanCrossAlways[Expression] {}
