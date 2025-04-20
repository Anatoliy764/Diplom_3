package kz.yandex.practicum.qa.sb.pom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConstructorPom {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/section[1]/h1")
    SelenideElement heading;

    public boolean isDisplayed() {
        return isHeadingDisplayed();
    }

    public boolean isHeadingDisplayed() {
        return Selenide.element(heading).shouldBe(Condition.visible).isDisplayed();
    }

    public String getHeading() {
        return heading.getText();
    }
}
