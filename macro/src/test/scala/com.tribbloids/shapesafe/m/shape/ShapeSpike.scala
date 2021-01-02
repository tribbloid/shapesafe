package com.tribbloids.shapesafe.m.shape

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.debug.print_@
import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.m.arity.Arity
import shapeless.{HNil, Witness}

class ShapeSpike extends BaseSpec {

  import shapeless.record._
  import shapeless.syntax.singleton.mkSingletonOps

  describe("records") {

    describe("example") {

      it("1") {

        val book =
          ("author" ->> "Benjamin Pierce") ::
            ("title" ->> "Types and Programming Languages") ::
            ("id" ->> 262162091) ::
            ("price" ->> 44.11) ::
            ("price" ->> 33.11) ::
            HNil

        print_@(VizType.infer(book).toString)

        {
          val rr = book.apply("author") // Note result type ...

          print_@(rr)
          print_@(VizType.infer(rr).toString)
        }

        {
          val rr = book.apply("price") // Note result type ...

          print_@(rr)
          print_@(VizType.infer(rr).toString)
        }

        {
          val values = book.values
        }
      }

      it("2") {

        val book =
          ("author" ->> "Benjamin Pierce") ::
            "Types and Programming Languages" ::
            ("id" ->> 262162091) ::
            ("price" ->> 44.11) ::
            ("price" ->> 33.11) ::
            HNil

        {
          val rr = book.apply("author") // Note result type ...

          print_@(rr)
          print_@(VizType.infer(rr).toString)
        }

        {
//          val values = book.values
        }
      }
    }

    it("3") {

      val field = "id" ->> 262162091

      print_@(VizType.infer(field).toString)
    }

    def asW_H(v: Witness.Lt[Symbol]*) = {}

    it("2") {

      val dims = {
        (Symbol("x") ->> Arity.FromLiteral(3)) ::
          (Symbol("y") ->> Arity.FromLiteral(4)) ::
          HNil
      }

      val x = dims.apply(Symbol("x"))

      {
        val fields = dims.fields
//        print_@(VizType.infer(dims))
//        print_@(VizType.infer(fields))
      }

      val values = dims.values
      val is = Symbol("i") :: Witness(Symbol("j")) :: HNil

      val reIndexed = is.zip(values).record

//      print_@(VizType.infer(reIndexed))
    }

    type A = shapeless._0
  }
}
