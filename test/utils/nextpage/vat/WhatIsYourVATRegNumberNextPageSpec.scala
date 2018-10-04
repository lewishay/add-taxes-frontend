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

package utils.nextpage.vat

import uk.gov.hmrc.auth.core.AffinityGroup
import utils.NextPage
import utils.nextpage.NextPageSpecBase

class WhatIsYourVATRegNumberNextPageSpec extends NextPageSpecBase {

  val affinityGroupOrganisation = Some(AffinityGroup.Organisation)
  val affinityGroupIndividual = Some(AffinityGroup.Individual)

  "whatIsYourVATRegNumber" when {
    behave like nextPage(
      NextPage.whatIsYourVATRegNumber,
      (true, "999999999", affinityGroupIndividual),
      "http://localhost:9566/vat-through-software/sign-up/claim-subscription/999999999"
    )

    behave like nextPage(
      NextPage.whatIsYourVATRegNumber,
      (false, "999999999", affinityGroupIndividual),
      "http://localhost:9555/enrolment-management-frontend/HMCE-VATDEC-ORG/request-access-tax-scheme?continue=%2Fbusiness-account"
    )

    behave like nextPage(
      NextPage.whatIsYourVATRegNumber,
      (true, "999999999", affinityGroupOrganisation),
      "/business-account/add-tax/vat/register-online"
    )
  }
}
