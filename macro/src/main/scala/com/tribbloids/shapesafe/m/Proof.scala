package com.tribbloids.shapesafe.m

import com.tribbloids.shapesafe.m.arity.Expression

trait Proof extends Serializable {

  type Out

  def out: Out
}
