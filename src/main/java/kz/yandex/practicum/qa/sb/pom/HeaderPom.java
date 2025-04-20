package kz.yandex.practicum.qa.sb.pom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class HeaderPom {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/header/nav/ul/li[1]/a")
    SelenideElement constructorAnchor;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/header/nav/a")
    SelenideElement personalAccountAnchor;

    public HeaderPom() {
        super();
    }

    public boolean isConstructorAnchorDisplayed() {
        return Selenide.element(constructorAnchor).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isPersonalAccountAnchorDisplayed() {
        return Selenide.element(personalAccountAnchor).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isDisplayed() {
        return isConstructorAnchorDisplayed() && isPersonalAccountAnchorDisplayed();
    }

    public <T extends Layout> T clickPersonalAccountAnchor(Class<T> clazz) {

        personalAccountAnchor.click();

        T page = Selenide.page(clazz);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> page.isDisplayed());

        return page;
    }

}
