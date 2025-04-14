package kz.yandex.practicum.qa.sb.pom;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

public class HeaderPom {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/header/nav/ul/li[1]/a")
    SelenideElement constructorAnchor;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/header/nav/a")
    SelenideElement accountAnchor;

    public <T extends Layout> T enterAccount() {
        accountAnchor.click();

        // todo : add support of open account page
        LoginPom loginPom = Selenide.page(LoginPom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> loginPom.isDisplayed());

        return (T) loginPom;
    }

}
