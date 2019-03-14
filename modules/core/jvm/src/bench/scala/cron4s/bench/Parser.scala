package cron4s
package bench

import cron4s.expr._

import org.scalameter.api._
import org.scalameter.picklers.Implicits._

object Parser extends Bench.OnlineRegressionReport {
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
