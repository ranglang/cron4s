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

import org.scalameter.api._
import org.scalameter.picklers.Implicits._

object ParserBenchmark extends Bench.OfflineRegressionReport {
  val expressions = Gen.enumeration("cron")(
    "10-35 2,4,6 * ? * *",
    "10-65 * * * * *",
    "0 0 0 * 4-10 ?",
    "10/2 2,4-14,18 * ? * */2",
  )

  performance of "Cron" in {
    measure method "parse" in {
      using(expressions) config (
        exec.minWarmupRuns      -> 10,
        exec.maxWarmupRuns      -> 20,
        exec.benchRuns          -> 30,
        exec.independentSamples -> 5
      ) in { expr =>
        Cron.parse(expr)
      }
    }
  }

}
