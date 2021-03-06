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

package cron4s.testkit.discipline

import cats.Eq
import cats.laws.discipline._
import cats.implicits._

import cron4s.CronField
import cron4s.datetime.IsDateTime
import cron4s.testkit.CronFieldValue
import cron4s.testkit.laws.IsDateTimeLaws

import org.scalacheck._
import Prop._

import org.typelevel.discipline.Laws

/**
  * Created by alonsodomin on 29/08/2016.
  */
trait IsDateTimeTests[DateTime] extends Laws {
  def laws: IsDateTimeLaws[DateTime]

  def dateTime[F <: CronField](
      implicit
      arbDateTime: Arbitrary[DateTime],
      arbFieldValue: Arbitrary[CronFieldValue[F]],
      arbField: Arbitrary[F]
  ): RuleSet =
    new DefaultRuleSet(
      name = "dateTime",
      parent = None,
      "gettable"     -> forAll(laws.gettable[F] _),
      "immutability" -> forAll(laws.immutability[F] _),
      "settable"     -> forAll(laws.settable[F] _)
    )

}

object IsDateTimeTests {

  def apply[DateTime](
      implicit
      dtEv: IsDateTime[DateTime],
      eqEv: Eq[DateTime]
  ): IsDateTimeTests[DateTime] =
    new IsDateTimeTests[DateTime] {
      val laws: IsDateTimeLaws[DateTime] = IsDateTimeLaws[DateTime]
    }

}
