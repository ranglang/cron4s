package cron4s
package bench

import cats.implicits._

import org.scalameter.Parameters
import org.scalameter.api._

import scala.util.Try

object GenUtils {

  def concat[A](gens: Gen[A]*): Gen[A] =
    new Gen[A] {
      def warmupset = gens.foldLeft(Iterator[A]())((acc, x) => acc ++ x.warmupset)
      def dataset   = gens.foldLeft(Iterator[Parameters]())((acc, x) => acc ++ x.dataset)
      def generate(params: Parameters) = {
        val paramMap = gens.foldLeft(Map.empty[Parameters, A])(
          (acc, g) => acc ++ g.dataset.map(p => p -> g.generate(p)).toMap
        )
        paramMap(params)
      }
      lazy val cardinality: Int = gens.map(_.cardinality).sum
    }.cached

}
