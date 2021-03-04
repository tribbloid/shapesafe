package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.debug.print_@
import com.tribbloids.graph.commons.util.viz.VizType
import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.LeafArity
import org.scalatest.Ignore
import shapeless.{HNil, Witness}

@Ignore
class LeafShapeSpike extends BaseSpec {

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

      it("3") {

        val field = "id" ->> 262162091

        print_@(VizType.infer(field).toString)
      }

      it("4") {
        {

          val fields =
            (Symbol("id").narrow -> 262162091) ::
              (Symbol("price").narrow -> 44.11) ::
              HNil

          val record = fields.record

          //        print_@(record.price)
        }

        {
          val fields =
            (Symbol("id") ->> 262162091) ::
              (Symbol("price") ->> 44.11) ::
              HNil

          val record = fields.record

          print_@(record.price)
        }

        // TODO: only works in shapeless 3.x
        {
          val fields =
            ("id" ->> 262162091) ::
              ("price" ->> 44.11) ::
              HNil

          val record = fields.record

          //        print_@(record.price)
        }

      }

      it("5") {
        val fields =
          ("id" ->> 262162091) ::
            ("price" ->> 44.11) ::
            HNil

        val keys = fields.keys

        print_@(VizType.infer(keys))
      }
    }

    def asW_H(v: Witness.Lt[Symbol]*) = {}

//    it("infer keys") {
//
//      val record = {
//        ("a" ->> 1) ::
//          ("b" ->> 2) ::
//          HNil
//      }
//
//      def inferKeys[T <: HList](v: T)(implicit keys: shapeless.ops.record.Keys[T]) = keys
//
//      {
//        val keys = record.keys // works
//        print(keys)
//
//        inferKeys(record) // works
//      }
//
//      {
//        val record2: record.type = record
//        val keys = record2.keys // works
//        print(keys)
//
//        inferKeys(record2) // works
////        inferKeys[record.type](record2) // compilation error!
//      }
//
//      VizType[record.type].toString().shouldBe()
//    }

    it("zip") {

      val dims = {
        (Symbol("x") ->> LeafArity.Literal(3)) ::
          (Symbol("y") ->> LeafArity.Literal(4)) ::
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

//    it("constraint") {
//
//      def
//    }
  }
}
