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

import org.scalameter.Parameters
import org.scalameter.api._

object GenUtils {

  def concat[A](gens: Gen[A]*): Gen[A] =
    new Gen[A] {
      def warmupset = gens.foldLeft(Iterator[A]())((acc, x) => acc ++ x.warmupset)
      def dataset   = gens.foldLeft(Iterator[Parameters]())((acc, x) => acc ++ x.dataset)
      def generate(params: Parameters) = {
        val paramMap = gens.foldLeft(Map.empty[Parameters, A])(
          (acc, g) => acc ++ g.dataset.map(p => p -> g.generate(p)).toMap
        )
        paramMap(params)
      }
      lazy val cardinality: Int = gens.map(_.cardinality).sum
    }.cached

}
