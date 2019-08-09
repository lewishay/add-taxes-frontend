/*
 * Copyright 2019 HM Revenue & Customs
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

package controllers.vat

import play.api.data.Form
import play.api.libs.json.JsString
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.FakeNavigator
import controllers.actions.{FakeServiceInfoAction, _}
import controllers._
import play.api.test.Helpers._
import forms.vat.ImportedGoodsFormProvider
import identifiers.ImportedGoodsId
import models.vat.ImportedGoods
import org.scalatest.BeforeAndAfterEach
import play.api.mvc.Call
import play.twirl.api.HtmlFormat
import playconfig.featuretoggle.{FeatureToggleSupport, NewVatJourney}
import uk.gov.hmrc.http.NotFoundException
import views.html.vat.importedGoods

class ImportedGoodsControllerSpec extends ControllerSpecBase with BeforeAndAfterEach with FeatureToggleSupport {

  def onwardRoute = controllers.routes.IndexController.onPageLoad()

  val formProvider = new ImportedGoodsFormProvider()
  val form = formProvider()

  def controller() =
    new ImportedGoodsController(
      frontendAppConfig,
      messagesApi,
      new FakeNavigator[Call](desiredRoute = onwardRoute),
      FakeAuthAction,
      FakeServiceInfoAction,
      formProvider,
      featureDepandantAction = app.injector.instanceOf[FeatureDependantAction]
    )

  def viewAsString(form: Form[_] = form) =
    importedGoods(frontendAppConfig, form)(HtmlFormat.empty)(fakeRequest, messages).toString

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(NewVatJourney)
  }

  "ImportedGoods Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ImportedGoods.options.head.value))

      val result = controller().onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "return OK" in {
      val result = controller().onPageLoad()(fakeRequest)

      status(result) mustBe OK
    }

    "return exception when newVatJourney is disabled" in {
      disable(NewVatJourney)
      intercept[NotFoundException] {
        val result = controller().onPageLoad()(fakeRequest)
        await(result)
      }
    }

    for (option <- ImportedGoods.options) {
      s"redirect to next page when '${option.value}' is submitted" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", (option.value)))
        val result = controller().onSubmit()(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }
  }
}