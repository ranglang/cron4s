package cron4s
package bench

import cron4s.expr._
import cron4s.validation.NodeValidator

import org.scalameter.api._
import org.scalameter.picklers.noPickler._

object NodeValidatorBenchmark extends Bench.OfflineRegressionReport {
  import CronField._

  override def persistor = new SerializationPersistor

  val severalNodes = Gen.enumeration("several")(
    SeveralNode(ConstNode[Second](35), ConstNode[Second](40))
  )

  val severalNodeValidator = NodeValidator[SeveralNode[Second]]

  performance of "NodeValidator[SeveralNode]" in {
    measure method "validate" in {
      using(severalNodes) in { node =>
        severalNodeValidator.validate(node)
      }
    }
  }
}
