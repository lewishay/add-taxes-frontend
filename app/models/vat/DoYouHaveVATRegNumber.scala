/*
 * Copyright 2018 HM Revenue & Customs
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

package models.vat

import play.api.libs.json.{Json, OFormat}
import utils.{Enumerable, RadioOption, WithName}

case class DoYouHaveVATRegNumber(value: Boolean, vrn: Option[String])

object DoYouHaveVATRegNumber {

  object Yes extends DoYouHaveVATRegNumber(true, Some(""))
  object No extends DoYouHaveVATRegNumber(false, None)

  val values: Set[DoYouHaveVATRegNumber] = Set(
    Yes,
    No
  )

  val options: Set[(String, String)] = values.map { value =>
    ("doYouHaveVATRegNumber", value.toString)
  }

  implicit val enumerable: Enumerable[DoYouHaveVATRegNumber] =
    Enumerable(values.toSeq.map(v => v.toString -> v): _*)

  implicit val format: OFormat[DoYouHaveVATRegNumber] = Json.format[DoYouHaveVATRegNumber]
}
