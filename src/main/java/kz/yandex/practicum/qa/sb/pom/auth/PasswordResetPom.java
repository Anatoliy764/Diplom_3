package kz.yandex.practicum.qa.sb.pom.auth;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import kz.yandex.practicum.qa.sb.pom.Layout;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class PasswordResetPom extends Layout {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/h2")
    SelenideElement pageTitle;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/fieldset/div/div/input")
    SelenideElement emailField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/button")
    SelenideElement resetButton;

    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEmail(String email) {
        emailField.setValue(email);
    }

    public String getEmail() {
        return emailField.getValue();
    }

    public boolean isEmailFieldDisplayed() {
        return Selenide.element(emailField).shouldBe(Condition.visible).isDisplayed();
    }

    public String getPageTitle() {
        return pageTitle.getText();
    }

    public boolean isResetButtonDisplayed() {
        return Selenide.element(resetButton).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isResetButtonClickable() {
        return Selenide.element(resetButton).shouldBe(Condition.enabled).isEnabled();
    }

    public boolean isDisplayed() {
        return Selenide.element(emailField).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(resetButton).shouldBe(Condition.visible).isDisplayed();
    }

}
