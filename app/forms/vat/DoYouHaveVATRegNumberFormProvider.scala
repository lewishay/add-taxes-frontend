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

package forms.vat

import javax.inject.Inject

import forms.FormErrorHelper
import forms.mappings.Mappings
import play.api.data.Form
import play.api.data.Forms._
import models.vat.DoYouHaveVATRegNumber
import play.api.data.validation.Constraints
import uk.gov.voa.play.form.ConditionalMappings._

class DoYouHaveVATRegNumberFormProvider @Inject() extends FormErrorHelper with Mappings {

  def apply(): Form[DoYouHaveVATRegNumber] = Form(
    tuple(
      "value" -> enumerable[DoYouHaveVATRegNumber]("doYouHaveVATRegNumber.error.required"),
      "vrn" -> mandatoryIf(
        isEqual("value", "Yes"),
        text("doYouHaveVATRegNumber.error.vrnRequired").verifying(Constraints.nonEmpty)
      )
    )
  )
}
