/*
 * Copyright 2017 Antonio Alonso Dominguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cron4s
package bench

import cron4s.expr._

import org.scalameter.api._
import org.scalameter.picklers.Implicits._

trait BenchmarkNodeGenerators {
  import CustomPicklers._

  def eachNodeGen[F <: CronField](implicit unit: CronUnit[F]): Gen[EachNode[F]] =
    Gen.single("*")("").map(_ => EachNode[F])

  def anyNodeGen[F <: CronField](implicit unit: CronUnit[F]): Gen[AnyNode[F]] =
    Gen.single("?")("").map(_ => AnyNode[F])

  def constNodeGen[F <: CronField](implicit unit: CronUnit[F]): Gen[ConstNode[F]] = {
    val values = Gen.enumeration(s"${unit.toString}")(unit.range: _*)
    values.map(ConstNode[F](_))
  }

  def betweenNodeGen[F <: CronField](implicit unit: CronUnit[F]): Gen[BetweenNode[F]] =
    for {
      begin <- constNodeGen[F]
      end   <- constNodeGen[F]
    } yield BetweenNode[F](begin, end)

  def severalNodeGen[F <: CronField](implicit unit: CronUnit[F]): Gen[SeveralNode[F]] = {
    def listOfEnumerableNodes: Gen[List[EnumerableNode[F]]] =
      for {
        size <- Gen.range("several-range")(unit.min + 1, unit.max, unit.max / 10)
        list <- Gen
          .single("several-list")((unit.min until size).toList)
          .map(_.map(ConstNode[F](_)))
      } yield list.map(const2Enumerable)

    listOfEnumerableNodes.map(SeveralNode.fromSeq[F](_).get)
  }

}
