package org.shapesafe.core

import com.tribbloids.graph.commons.util.reflect.Reflection
import com.tribbloids.graph.commons.util.viz.TypeViz
import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.{Arity, LeafArity}

class CompileTimeInfoSpec extends BaseSpec {

  import org.shapesafe.core.arity.ops.ArityOps._

  val infoViz: TypeViz[Reflection.Runtime.type] = TypeViz.withFormat(CompileTimeInfo.defaultFormat)
//  val viz = TypeViz

  describe("runtime") {

    describe(classOf[LeafArity].getSimpleName) {

      it("1") {
        val a = Arity(3)
        infoViz[a._Arity].typeStr.shouldBe("3")
      }

      it("2") {
        val a = (Arity(3) :+ Arity(4)).eval
        infoViz[a._Arity].typeStr.shouldBe("7")
      }

      it("3") {
        val a = LeafArity.Var(3).^
        infoViz[a._Arity].typeStr.shouldBe("LeafArity.Var")
      }
    }

    it(:==!.toString) {
      val a = Arity(3) :==! Arity(4)
      infoViz[a._Arity].typeStr.shouldBe("3 :==! 4")

    }

    describe(:+.toString) {

      it("1") {
        val a = Arity(3) :+ Arity(4)
        infoViz[a._Arity].typeStr.shouldBe("3 :+ 4")
      }

      it("2") {
        val a = Arity(3) :+ LeafArity.Var(3).^
        infoViz[a._Arity].typeStr.shouldBe("3 :+ LeafArity.Var")
      }
    }
  }
}
