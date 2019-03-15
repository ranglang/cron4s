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

import cats.syntax.show._

import org.scalameter.api._
import org.scalameter.picklers.Implicits._

trait BenchmarkExprGenerators {

  val anyExprGen: Gen[String]  = Gen.single("any")("*")
  val eachExprGen: Gen[String] = Gen.single("each")("?")

  def constExprGen[F <: CronField](implicit unit: CronUnit[F]): Gen[String] =
    Gen.enumeration(s"const[${unit.show}]")(unit.range.toList: _*).map(_.toString)

  def betweenExprGen[F <: CronField](implicit unit: CronUnit[F]): Gen[String] =
    for {
      begin <- constExprGen[F]
      end   <- constExprGen[F]
    } yield s"$begin-$end"

  def severalExprGen[F <: CronField](implicit unit: CronUnit[F]): Gen[String] = {
    val size = Gen.range("several-size")(unit.min + 1, unit.max, 1)
    size.map(n => (unit.min to n).mkString(","))
  }

  def everyExprGen[F <: CronField](implicit unit: CronUnit[F]): Gen[String] = {
    val divends = Gen.range("every-dividend")(0, unit.max, 1).map(_.toString)

    val usingBetween = for {
      betweenExpr <- betweenExprGen[F]
      dividend    <- divends
    } yield s"$betweenExpr/$dividend"

    val usingSeveral = for {
      severalExpr <- severalExprGen[F]
      dividend    <- divends
    } yield s"$severalExpr/$dividend"

    GenUtils.concat(usingBetween, usingSeveral)
  }

}
