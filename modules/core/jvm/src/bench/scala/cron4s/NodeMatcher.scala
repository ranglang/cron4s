package cron4s

import cron4s.expr._

import org.scalameter.api._
import org.scalameter.picklers.noPickler._

object NodeMatcher extends Bench.LocalTime {
  final val ValueToMatch = 30

  val eachNode  = EachNode[CronField.Minute]
  val constNode = ConstNode[CronField.Minute](30)
  val betweenNode = BetweenNode(
    ConstNode[CronField.Minute](CronUnit.Minutes.min),
    ConstNode[CronField.Minute](CronUnit.Minutes.max)
  )
  val severalEnumeratedNode = {
    val minutes = for {
      value <- CronUnit.Minutes.range
    } yield const2Enumerable(ConstNode[CronField.Minute](value))
    SeveralNode.fromSeq(minutes).get
  }

  def severalByChunkSize[F <: CronField](chunkSize: Int)(f: (Int, Int) => EnumerableNode[F])(implicit unit: CronUnit[F]): SeveralNode[F] = {
    val minuteRanges = (unit.min to unit.max by chunkSize)
      .map { lower => f(lower, chunkSize) }

    SeveralNode.fromSeq[F](minuteRanges).get
  }

  def severalNodeGen[F <: CronField](implicit unit: CronUnit[F]) = {
    val stepSize = unit.max / 10
    Gen.range("severalGap")(unit.min, unit.max, stepSize)
  }

  val nodes = Gen.enumeration("node")(
    eachNode,
    constNode,
    betweenNode,
    severalEnumeratedNode,
    SeveralNode(constNode, betweenNode),
    EveryNode(eachNode, 1),
  )

  performance of "Node" in {
    measure method "match" in {
      using(nodes) config (
        exec.minWarmupRuns -> 10,
        exec.maxWarmupRuns -> 20,
        exec.benchRuns -> 30,
        exec.independentSamples -> 5
      ) in {
        n => n.matches(ValueToMatch)
      }
    }
  }
}
