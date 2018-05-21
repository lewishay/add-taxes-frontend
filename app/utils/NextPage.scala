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

package utils

import config.FrontendAppConfig
import controllers.other.importexports.dan.{routes => danRoutes}
import controllers.other.importexports.ebti.{routes => ebtiRoutes}
import controllers.other.importexports.emcs.{routes => emcsRoutes}
import controllers.other.importexports.ics.{routes => icsRoutes}
import controllers.other.importexports.ncts.{routes => nctsRoutes}
import controllers.other.importexports.nes.{routes => nesRoutes}
import controllers.other.oil.routes
import controllers.sa.partnership.{routes => saPartnerRoutes}
import controllers.sa.trust.{routes => trustRoutes}
import identifiers._
import models.OtherTaxes
import models.other.gambling.gbd.AreYouRegisteredGTS
import models.other.importexports.dan.DoYouHaveDAN
import models.other.importexports.emcs.DoYouHaveASEEDNumber
import models.other.importexports.nes.DoYouHaveCHIEFRole
import models.other.importexports.{DoYouHaveEORINumber, DoYouWantToAddImportExport}
import models.sa.SelectSACategory
import models.sa.partnership.DoYouWantToAddPartner
import models.sa.trust.HaveYouRegisteredTrust
import models.vat.moss.uk.OnlineVATAccount
import models.wrongcredentials.FindingYourAccount
import play.api.mvc.{Call, Request}
import utils.nextpage.OtherTaxesNextPage
import utils.nextpage.employer.cis.uk.contractor.{DoesBusinessManagePAYENextPage, IsBusinessRegisteredForPAYENextPage}
import utils.nextpage.other.aeoi.HaveYouRegisteredAEOINextPage
import utils.nextpage.other.alcohol.atwd.AreYouRegisteredWarehousekeeperNextPage
import utils.nextpage.other.alcohol.awrs.SelectAlcoholSchemeNextPage
import utils.nextpage.other.charity.DoYouHaveCharityReferenceNextPage
import utils.nextpage.other.gambling.{AreYouRegisteredGTSNextPage, SelectGamblingOrGamingDutyNextPage}
import utils.nextpage.other.gambling.mgd.DoYouHaveMGDRegistrationNextPage
import utils.nextpage.other.importexports.DoYouHaveEORINumberNextPage
import utils.nextpage.other.oil.{HaveYouRegisteredForRebatedOilsNextPage, HaveYouRegisteredForTiedOilsNextPage, SelectAnOilServiceNextPage}
import utils.nextpage.sa.SelectSACategoryNextPage
import utils.nextpage.vat.moss.uk.{OnlineVATAccountNextPage, RegisteredForVATUKNextPage}
import utils.nextpage.wrongcredentials.FindingYourAccountNextPage

trait NextPage[A, B] {
  def get(b: B)(implicit appConfig: FrontendAppConfig, request: Request[_]): Call
}

object NextPage
    extends HaveYouRegisteredAEOINextPage
    with DoYouHaveCharityReferenceNextPage
    with DoYouHaveMGDRegistrationNextPage
    with AreYouRegisteredWarehousekeeperNextPage
    with SelectGamblingOrGamingDutyNextPage
    with HaveYouRegisteredForRebatedOilsNextPage
    with IsBusinessRegisteredForPAYENextPage
    with HaveYouRegisteredForTiedOilsNextPage
    with DoesBusinessManagePAYENextPage
    with SelectAnOilServiceNextPage
    with SelectAlcoholSchemeNextPage
    with FindingYourAccountNextPage
    with RegisteredForVATUKNextPage
    with OtherTaxesNextPage
    with OnlineVATAccountNextPage
    with SelectSACategoryNextPage
    with DoYouHaveEORINumberNextPage
    with AreYouRegisteredGTSNextPage {

  implicit val whichPensionSchemeToAdd
    : NextPage[WhichPensionSchemeToAddId.type, models.employer.pension.WhichPensionSchemeToAdd] = {
    new NextPage[WhichPensionSchemeToAddId.type, models.employer.pension.WhichPensionSchemeToAdd] {
      override def get(b: models.employer.pension.WhichPensionSchemeToAdd)(
        implicit appConfig: FrontendAppConfig,
        request: Request[_]): Call =
        b match {
          case models.employer.pension.WhichPensionSchemeToAdd.Administrators =>
            Call(GET, appConfig.getPortalUrl("pensionAdministrators"))
          case models.employer.pension.WhichPensionSchemeToAdd.Practitioners =>
            Call(GET, appConfig.getPortalUrl("pensionPractitioners"))
        }
    }
  }

  implicit val doYouWantToAddPartner: NextPage[DoYouWantToAddPartnerId.type, DoYouWantToAddPartner] = {
    new NextPage[DoYouWantToAddPartnerId.type, DoYouWantToAddPartner] {
      override def get(b: DoYouWantToAddPartner)(implicit appConfig: FrontendAppConfig, request: Request[_]): Call =
        b match {
          case DoYouWantToAddPartner.Yes => Call(GET, appConfig.getPublishedAssetsUrl("partnership"))
          case DoYouWantToAddPartner.No  => saPartnerRoutes.HaveYouRegisteredPartnershipController.onPageLoad()
        }
    }
  }

  implicit val haveYouRegisteredPartnership
    : NextPage[HaveYouRegisteredPartnershipId.type, models.sa.partnership.HaveYouRegisteredPartnership] = {
    new NextPage[HaveYouRegisteredPartnershipId.type, models.sa.partnership.HaveYouRegisteredPartnership] {
      override def get(b: models.sa.partnership.HaveYouRegisteredPartnership)(
        implicit appConfig: FrontendAppConfig,
        request: Request[_]): Call =
        b match {
          case models.sa.partnership.HaveYouRegisteredPartnership.Yes =>
            Call(GET, appConfig.emacEnrollmentsUrl(Enrolments.SAPartnership))
          case models.sa.partnership.HaveYouRegisteredPartnership.No =>
            Call(GET, appConfig.getPublishedAssetsUrl("partnershipOther"))
        }
    }
  }

  implicit val haveYouRegisteredTrust: NextPage[HaveYouRegisteredTrustId.type, HaveYouRegisteredTrust] = {
    new NextPage[HaveYouRegisteredTrustId.type, HaveYouRegisteredTrust] {
      override def get(b: HaveYouRegisteredTrust)(implicit appConfig: FrontendAppConfig, request: Request[_]): Call =
        b match {
          case HaveYouRegisteredTrust.Yes => Call(GET, appConfig.emacEnrollmentsUrl(Enrolments.RegisterTrusts))
          case HaveYouRegisteredTrust.No  => trustRoutes.RegisterTrustController.onPageLoad()
        }
    }
  }

  implicit val doYouWantToAddImportExport: NextPage[DoYouWantToAddImportExportId.type, DoYouWantToAddImportExport] = {
    new NextPage[DoYouWantToAddImportExportId.type, models.other.importexports.DoYouWantToAddImportExport] {
      override def get(b: models.other.importexports.DoYouWantToAddImportExport)(
        implicit appConfig: FrontendAppConfig,
        request: Request[_]): Call =
        b match {
          case DoYouWantToAddImportExport.EMCS => emcsRoutes.DoYouHaveASEEDNumberController.onPageLoad()
          case DoYouWantToAddImportExport.ICS  => icsRoutes.DoYouHaveEORINumberController.onPageLoad()
          case DoYouWantToAddImportExport.DDES => danRoutes.DoYouHaveDANController.onPageLoad()
          case DoYouWantToAddImportExport.NOVA => Call(GET, appConfig.getPortalUrl("novaEnrolment"))
          case DoYouWantToAddImportExport.NCTS => nctsRoutes.DoYouHaveEORINumberController.onPageLoad()
          case DoYouWantToAddImportExport.eBTI => ebtiRoutes.DoYouHaveEORINumberController.onPageLoad()
          case DoYouWantToAddImportExport.NES  => nesRoutes.DoYouHaveEORINumberController.onPageLoad()
          case DoYouWantToAddImportExport.ISD  => Call(GET, appConfig.getHmceURL("isd"))
        }
    }
  }

  implicit val doYouHaveCHIEFHasEORIRole: NextPage[DoYouHaveCHIEFRoleId.HasEORI.type, DoYouHaveCHIEFRole] = {
    new NextPage[DoYouHaveCHIEFRoleId.HasEORI.type, DoYouHaveCHIEFRole] {
      override def get(b: DoYouHaveCHIEFRole)(implicit appConfig: FrontendAppConfig, request: Request[_]): Call =
        b match {
          case DoYouHaveCHIEFRole.Yes => Call(GET, appConfig.emacEnrollmentsUrl(Enrolments.NewExportSystem))
          case DoYouHaveCHIEFRole.No  => nesRoutes.GetCHIEFRoleController.onPageLoad()
        }
    }
  }

  implicit val doYouHaveCHIEFNoEORIRole: NextPage[DoYouHaveCHIEFRoleId.NoEORI.type, DoYouHaveCHIEFRole] = {
    new NextPage[DoYouHaveCHIEFRoleId.NoEORI.type, DoYouHaveCHIEFRole] {
      override def get(b: DoYouHaveCHIEFRole)(implicit appConfig: FrontendAppConfig, request: Request[_]): Call =
        b match {
          case DoYouHaveCHIEFRole.Yes => nesRoutes.RegisterEORIController.onPageLoad()
          case DoYouHaveCHIEFRole.No  => nesRoutes.GetEoriAndChiefRoleController.onPageLoad()
        }
    }
  }

  implicit val doYouHaveDAN: NextPage[DoYouHaveDANId.type, DoYouHaveDAN] = {
    new NextPage[DoYouHaveDANId.type, DoYouHaveDAN] {
      override def get(b: DoYouHaveDAN)(implicit appConfig: FrontendAppConfig, request: Request[_]): Call =
        b match {
          case DoYouHaveDAN.Yes => Call(GET, appConfig.emacEnrollmentsUrl(Enrolments.DefermentApprovalNumber))
          case DoYouHaveDAN.No  => danRoutes.RegisterDefermentApprovalNumberController.onPageLoad()
        }
    }
  }

  implicit val doYouHaveASEEDNumber: NextPage[DoYouHaveASEEDNumberId.type, DoYouHaveASEEDNumber] = {
    new NextPage[DoYouHaveASEEDNumberId.type, DoYouHaveASEEDNumber] {
      override def get(b: DoYouHaveASEEDNumber)(implicit appConfig: FrontendAppConfig, request: Request[_]): Call =
        b match {
          case DoYouHaveASEEDNumber.Yes =>
            Call(GET, appConfig.emacEnrollmentsUrl(Enrolments.ExciseMovementControlSystem))
          case DoYouHaveASEEDNumber.No => emcsRoutes.RegisterExciseMovementControlSystemController.onPageLoad()
        }
    }
  }

  private val GET: String = "GET"
}
