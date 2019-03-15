package cron4s
package bench

import org.scalameter.picklers._

object CustomPicklers {
  implicit def listPickler[A](implicit pickler: Pickler[A]): Pickler[List[A]] =
    new Pickler[List[A]] {
      def pickle(x: List[A]): Array[Byte] =
        x.foldLeft(Array.empty[Byte])((acc, y) => acc ++ pickler.pickle(y))

      def unpickle(x: Array[Byte], from: Int): (List[A], Int) = {
        val (list, newIdx) = x.foldLeft((List.empty[A], from)) {
          case ((acc, prevIdx), _) =>
            val (item, idx) = pickler.unpickle(x, prevIdx)
            (item :: acc, idx)
        }

        (list.reverse, newIdx)
      }
    }
}
