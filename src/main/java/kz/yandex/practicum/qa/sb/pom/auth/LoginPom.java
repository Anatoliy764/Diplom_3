package kz.yandex.practicum.qa.sb.pom.auth;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import kz.yandex.practicum.qa.sb.pom.HomePom;
import kz.yandex.practicum.qa.sb.pom.Layout;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginPom extends Layout {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/h2")
    SelenideElement pageTitle;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/fieldset[1]/div/div/input")
    SelenideElement emailField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/fieldset[2]/div/div/input")
    SelenideElement passwordField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/button")
    SelenideElement entranceButton;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/div/p[2]/a")
    SelenideElement resetPasswordAnchor;

    public LoginPom() {
        super();
    }

    public void setEmail(String email) {
        emailField.setValue(email);
    }

    public void setPassword(String password) {
        passwordField.setValue(password);
    }

    public String getEmail() {
        return emailField.getValue();
    }

    public String getPassword() {
        return passwordField.getValue();
    }

    public HomePom clickEntranceButton() {
        entranceButton.click();

        HomePom homePom = Selenide.page(HomePom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> homePom.isDisplayed());

        return homePom;
    }

    public ResetPasswordPom clickResetPasswordAnchor() {
        resetPasswordAnchor.click();

        ResetPasswordPom resetPasswordPom = Selenide.page(ResetPasswordPom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> resetPasswordPom.isDisplayed());

        return resetPasswordPom;
    }

    public boolean isEmailFieldDisplayed() {
        return Selenide.element(emailField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isPasswordFieldDisplayed() {
        return Selenide.element(passwordField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isEntranceButtonDisplayed() {
        return Selenide.element(entranceButton).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isEntranceButtonClickable() {
        return Selenide.element(entranceButton).shouldBe(Condition.enabled).isEnabled();
    }

    public boolean isDisplayed() {
        return Selenide.element(entranceButton).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(emailField).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(passwordField).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(pageTitle).shouldBe(Condition.visible).isDisplayed();
    }

    public String getPageTitle() {
        return pageTitle.getText();
    }

    @Override
    public String toString() {
        return "LoginPom{" +
               "emailField=" + getEmail() +
               ", passwordFiled=" + getPassword() +
               '}';
    }
}
