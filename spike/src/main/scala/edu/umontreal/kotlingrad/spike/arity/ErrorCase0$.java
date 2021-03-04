package com.tribbloids.shapesafe.m.arity.spike;// https://github.com/fthomas/singleton-ops/issues/152
        object ErrorCase0 {

                trait Proof extends Serializable {

                        type Out <: Arity
                        def out: Out
                        }

                trait Arity extends Proof {

                        override type Out = this.type
                        override def out: Out = this
                        }

                object Arity {

                        trait Const[S] extends Arity with Proof {

                                type SS = S
                                }

                        case class FromLiteral[S <: Int]() extends Const[S] {}
                        }

                val a = Arity.FromLiteral[Witness.`6`.T]()
                //  val f1 = Arity(6)
                implicitly[a.SS + Witness.`3`.T]

                val aa = a.out
                //  implicitly[v2.SS + Witness.`3`.T] // this fails
                }
