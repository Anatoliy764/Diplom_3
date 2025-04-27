package kz.yandex.practicum.qa.sb;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

class SelenideTets {


    @Test
    void test() {
        SelenideBrowserConfigurator.configure();
        LoginPom login = Selenide.open("https://stellarburgers.nomoreparties.site/login", LoginPom.class);



        System.out.println(login);
    }


}
