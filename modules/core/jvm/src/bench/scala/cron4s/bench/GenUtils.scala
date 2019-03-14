package cron4s
package bench

import cats.implicits._

import org.scalameter.Parameters
import org.scalameter.api._

import scala.util.Try

object GenUtils {

  def concat[A](gens: Gen[A]*): Gen[A] =
    new Gen[A] {
      def axes      = dataset.map(_.axisData).reduce(_ ++ _).keys.toSet
      def warmupset = gens.foldLeft(Iterator[A]())((acc, x) => acc ++ x.warmupset)
      def dataset   = gens.foldLeft(Iterator[Parameters]())((acc, x) => acc ++ x.dataset)
      def generate(params: Parameters) = {
        val paramMap = gens.foldLeft(Map.empty[Parameters, A])(
          (acc, g) =>
            acc ++ g.dataset
              .flatMap(p => {
                Try(g.generate(p)).map(Seq(_).iterator).getOrElse(Iterator[A]()).map(p -> _)
              })
              .toMap
        )
        println(paramMap)
        paramMap(params)
      }
      lazy val cardinality: Int = gens.map(_.cardinality).sum
    }.cached

}
