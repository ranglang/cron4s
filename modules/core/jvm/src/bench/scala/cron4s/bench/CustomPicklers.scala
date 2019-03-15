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

import org.scalameter.picklers._

import scala.collection.mutable.ListBuffer

object CustomPicklers {
  implicit def listPickler[A](implicit pickler: Pickler[A]): Pickler[List[A]] =
    new Pickler[List[A]] {

      def pickle(x: List[A]): Array[Byte] = {
        val buffer = ListBuffer.empty[Byte]
        buffer ++= IntPickler.pickle(x.length)

        val iterator = x.iterator
        while (iterator.hasNext) {
          buffer ++= pickler.pickle(iterator.next())
        }
        buffer.toArray
      }

      def unpickle(x: Array[Byte], from: Int): (List[A], Int) = {
        val (length, headerIdx) = IntPickler.unpickle(x, from)
        val listBuffer          = ListBuffer.empty[A]

        var nextIdx   = headerIdx
        var elemCount = 0
        while (elemCount < length) {
          val (elem, elemIdx) = pickler.unpickle(x, nextIdx)
          listBuffer += elem
          nextIdx = elemIdx
          elemCount += 1
        }

        (listBuffer.toList, nextIdx)
      }
    }
}
