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

package cron4s.lib.joda

import cron4s.testkit.IsDateTimeTestKit
import org.joda.time.{DateTime, LocalDate, LocalDateTime, LocalTime}

/**
  * Created by alonsodomin on 04/02/2017.
  */
class JodaDateTimeSpec  extends IsDateTimeTestKit[DateTime]("DateTime") with JodaDateTimeTestBase
class JodaLocalDateSpec extends IsDateTimeTestKit[LocalDate]("LocalDate") with JodaLocalDateTestBase
class JodaLocalTimeSpec extends IsDateTimeTestKit[LocalTime]("LocalTime") with JodaLocalTimeTestBase
class JodaLocalDateTimeSpec
    extends IsDateTimeTestKit[LocalDateTime]("LocalDateTime") with JodaLocalDateTimeTestBase
