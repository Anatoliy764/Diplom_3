package kz.yandex.practicum.qa.sb.pom.auth;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import kz.yandex.practicum.qa.sb.pom.main.HomePom;
import kz.yandex.practicum.qa.sb.pom.main.Layout;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginPom extends Layout {

    @FindBy(how = How.XPATH, using = "//h2[text()='Вход']")
    SelenideElement pageTitle;

    @FindBy(how = How.XPATH, using = "//label[text()='Email']/following-sibling::input")
    SelenideElement emailField;

    @FindBy(how = How.CSS, using = "input.text_type_main-default[name='Пароль']")
    SelenideElement passwordField;

    @FindBy(how = How.XPATH, using = "//button[text()='Войти']")
    SelenideElement entranceButton;

    @FindBy(how = How.XPATH, using = "//a[text()='Восстановить пароль']")
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

    @Step("Аутентификация пользователя")
    public HomePom clickEntranceButton() {
        entranceButton.click();

        HomePom homePom = Selenide.page(HomePom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> homePom.isDisplayed());

        return homePom;
    }

    @Step("Переход на страницу восстановления пароля")
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
