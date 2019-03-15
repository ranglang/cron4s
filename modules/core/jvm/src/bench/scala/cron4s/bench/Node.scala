package cron4s
package bench

import cron4s._
import cron4s.expr._

import org.scalameter.api._
import org.scalameter.picklers.noPickler._

object NodeBenchmark extends Bench.OfflineRegressionReport {
  override def persistor = new SerializationPersistor

  final val ValueToMatch = 30

  val eachNode  = EachNode[CronField.Minute]
  val constNode = ConstNode[CronField.Minute](30)
  val betweenNode = BetweenNode(
    ConstNode[CronField.Minute](CronUnit.Minutes.min),
    ConstNode[CronField.Minute](CronUnit.Minutes.max)
  )

  def severalByChunkSize[F <: CronField](
      f: (Int, Int) => EnumerableNode[F]
  )(implicit unit: CronUnit[F]): SeveralNode[F] = {
    val stepSize = unit.max / 10
    val minuteRanges = (unit.min to unit.max by stepSize)
      .map { lower =>
        f(lower, stepSize)
      }

    SeveralNode.fromSeq[F](minuteRanges).get
  }

  val severalConstNode = severalByChunkSize((n, _) => ConstNode[CronField.Minute](n))
  val severalBetweenNode = severalByChunkSize(
    (l, s) => BetweenNode[CronField.Minute](ConstNode(l), ConstNode(l + s - 1))
  )

  val nodes = Gen.enumeration("node")(
    eachNode,
    constNode,
    betweenNode,
    SeveralNode(constNode, betweenNode),
    severalConstNode,
    severalBetweenNode,
    EveryNode(eachNode, 1),
    EveryNode(severalConstNode, 10),
    EveryNode(severalBetweenNode, 10),
  )

  performance of "Node" in {
    measure method "match" in {
      using(nodes) in { n =>
        n.matches(ValueToMatch)
      }
    }

    measure method "range" in {
      using(nodes) in (_.range)
    }
  }

}
