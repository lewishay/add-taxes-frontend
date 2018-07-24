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

package controllers.deenrolment

import play.api.data.Form
import play.api.libs.json.JsString
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.FakeNavigator
import controllers.actions.{FakeServiceInfoAction, _}
import controllers._
import play.api.test.Helpers._
import forms.deenrolment.DoYouNeedToLeaveVATMOSSFormProvider
import identifiers.DoYouNeedToLeaveVATMOSSId
import models.deenrolment.DoYouNeedToLeaveVATMOSS
import play.api.mvc.Call
import play.twirl.api.HtmlFormat
import views.html.deenrolment.doYouNeedToLeaveVATMOSS

class DoYouNeedToLeaveVATMOSSControllerSpec extends ControllerSpecBase {

  def onwardRoute = controllers.routes.IndexController.onPageLoad()

  val formProvider = new DoYouNeedToLeaveVATMOSSFormProvider()
  val form = formProvider()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new DoYouNeedToLeaveVATMOSSController(
      frontendAppConfig,
      messagesApi,
      new FakeNavigator[Option[Call]](desiredRoute = Some(onwardRoute)),
      FakeAuthAction,
      FakeServiceInfoAction,
      formProvider)

  def viewAsString(form: Form[_] = form) =
    doYouNeedToLeaveVATMOSS(frontendAppConfig, form)(HtmlFormat.empty)(fakeRequest, messages).toString

  "DoYouNeedToLeaveVATMOSS Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", DoYouNeedToLeaveVATMOSS.options.head.value))

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

    "return OK if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad()(fakeRequest)

      status(result) mustBe OK
    }

    for (option <- DoYouNeedToLeaveVATMOSS.options) {
      s"redirect to next page when '${option.value}' is submitted and no existing data is found" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", (option.value)))
        val result = controller(dontGetAnyData).onSubmit()(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }
  }
}