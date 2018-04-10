#!/bin/bash

echo "Applying migration RegisterEmployers"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /registerEmployers               controllers.RegisterEmployersController.onPageLoad()" >> ../conf/app.routes
echo "POST       /registerEmployers               controllers.RegisterEmployersController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages (English)"
echo "" >> ../conf/messages.en
echo "#######################################################" >> ../conf/messages.en
echo "##  RegisterEmployers" >> ../conf/messages.en
echo "#######################################################" >> ../conf/messages.en
echo "registerEmployers.title = registerEmployers" >> ../conf/messages.en
echo "registerEmployers.heading = registerEmployers" >> ../conf/messages.en
echo "registerEmployers.option1 = registerEmployers" Option 1 >> ../conf/messages.en
echo "registerEmployers.option2 = registerEmployers" Option 2 >> ../conf/messages.en
echo "registerEmployers.checkYourAnswersLabel = registerEmployers" >> ../conf/messages.en
echo "registerEmployers.error.required = Please give an answer for registerEmployers" >> ../conf/messages.en

echo "Adding messages to conf.messages (Welsh)"
echo "" >> ../conf/messages.cy
echo "#######################################################" >> ../conf/messages.cy
echo "##  RegisterEmployers" >> ../conf/messages.cy
echo "#######################################################" >> ../conf/messages.cy
echo "registerEmployers.title = WELSH NEEDED HERE" >> ../conf/messages.cy
echo "registerEmployers.heading = WELSH NEEDED HERE" >> ../conf/messages.cy
echo "registerEmployers.option1 = WELSH NEEDED HERE" >> ../conf/messages.cy
echo "registerEmployers.option2 = WELSH NEEDED HERE" >> ../conf/messages.cy
echo "registerEmployers.checkYourAnswersLabel = WELSH NEEDED HERE" >> ../conf/messages.cy
echo "registerEmployers.error.required = WELSH NEEDED HERE" >> ../conf/messages.cy

echo "Adding navigation default to NextPage Object"
awk '/object/ {\
     print;\
     print "";\
     print "  implicit val registerEmployers: NextPage[RegisterEmployersId.type,";\
     print "    RegisterEmployers] = {";\
     print "    new NextPage[RegisterEmployersId.type, RegisterEmployers] {";\
     print "      override def get(b: RegisterEmployers)(implicit urlHelper: UrlHelper): Call =";\
     print "        b match {";\
     print "          case models.RegisterEmployers.Option1 => routes.IndexController.onPageLoad()";\
     print "          case models.RegisterEmployers.Option2 => routes.IndexController.onPageLoad()";\
     print "        }";\
     print "     }";\
     print "  }";\
     next }1' ../app/utils/NextPage.scala > tmp && mv tmp ../app/utils/NextPage.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration RegisterEmployers completed"
