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

package controllers.other.importexports.nes

import controllers._
import controllers.actions._
import forms.other.importexports.DoYouHaveEORINumberFormProvider
import models.other.importexports.DoYouHaveEORINumber
import play.api.data.Form
import play.api.test.Helpers._
import play.twirl.api.HtmlFormat
import utils.FakeNavigator
import viewmodels.ViewAction
import views.html.other.importexports.doYouHaveEORINumber

class DoYouHaveEORINumberControllerSpec extends ControllerSpecBase {

  def onwardRoute = controllers.routes.IndexController.onPageLoad()

  val formProvider = new DoYouHaveEORINumberFormProvider()
  val form = formProvider()
  val viewAction = ViewAction(routes.DoYouHaveEORINumberController.onSubmit(), "AddNESTax")

  def controller =
    new DoYouHaveEORINumberController(frontendAppConfig, messagesApi, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction,
      FakeServiceInfoAction, formProvider)

  def viewAsString(form: Form[_] = form) =
    doYouHaveEORINumber(frontendAppConfig, form, viewAction)(HtmlFormat.empty)(fakeRequest, messages).toString

  "DoYouHaveEORINumber Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller.onPageLoad(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", DoYouHaveEORINumber.options.head.value))

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    for(option <- DoYouHaveEORINumber.options) {
      s"redirect to next page when '${option.value}' is submitted and no existing data is found" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", option.value))
        val result = controller.onSubmit()(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }
  }
}




