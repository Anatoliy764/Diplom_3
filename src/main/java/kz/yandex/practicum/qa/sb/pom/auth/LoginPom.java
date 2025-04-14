package kz.yandex.practicum.qa.sb.pom.auth;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import kz.yandex.practicum.qa.sb.pom.HomePom;
import kz.yandex.practicum.qa.sb.pom.Layout;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginPom extends Layout {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/h2")
    SelenideElement pageTitle;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/fieldset[1]/div/div/input")
    SelenideElement emailField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/fieldset[2]/div/div/input")
    SelenideElement passwordField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/button")
    SelenideElement enterButton;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/div/p[2]/a")
    SelenideElement resetPasswordAnchor;

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

    public HomePom enter() {
        enterButton.click();

        HomePom homePom = Selenide.page(HomePom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> homePom.isDisplayed());

        return homePom;
    }

    public PasswordResetPom resetPassword() {
        resetPasswordAnchor.click();

        PasswordResetPom passwordResetPom = Selenide.page(PasswordResetPom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> passwordResetPom.isDisplayed());

        return passwordResetPom;
    }

    public boolean isEmailFieldDisplayed() {
        return Selenide.element(emailField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isPasswordFieldDisplayed() {
        return Selenide.element(passwordField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isEnterButtonDisplayed() {
        return Selenide.element(enterButton).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isEnterButtonClickable() {
        return Selenide.element(enterButton).shouldBe(Condition.enabled).isEnabled();
    }

    public boolean isDisplayed() {
        return Selenide.element(enterButton).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(emailField).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(passwordField).shouldBe(Condition.visible).isDisplayed();
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
