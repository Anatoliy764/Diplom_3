package kz.yandex.practicum.qa.sb.pom.auth;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import kz.yandex.practicum.qa.sb.pom.main.Layout;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterPom extends Layout {

    @FindBy(how = How.XPATH, using = "//h2[text()='Регистрация']")
    SelenideElement pageTitle;

    @FindBy(how = How.XPATH, using = "//label[text()='Имя']/following-sibling::input")
    SelenideElement nameField;

    @FindBy(how = How.XPATH, using = "//label[text()='Email']/following-sibling::input")
    SelenideElement emailField;

    @FindBy(how = How.CSS, using = "input.text_type_main-default[name='Пароль']")
    SelenideElement passwordField;

    @FindBy(how = How.XPATH, using = "//button[text()='Зарегистрироваться']")
    SelenideElement registerButton;

    @FindBy(how = How.XPATH, using = "//a[text()='Войти']")
    SelenideElement entranceAnchor;

    public RegisterPom() {
        super();
    }

    public void setName(String name) {
        nameField.setValue(name);
    }

    public void setEmail(String email) {
        emailField.setValue(email);
    }

    public void setPassword(String password) {
        passwordField.setValue(password);
    }

    public String getName() {
        return nameField.getValue();
    }

    public String getEmail() {
        return emailField.getValue();
    }

    public String getPassword() {
        return passwordField.getValue();
    }

    @Step("Регистрация пользователя")
    public LoginPom clickRegisterButton() {
        registerButton.click();

        LoginPom loginPom = Selenide.page(LoginPom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> loginPom.isDisplayed());

        return loginPom;
    }

    @Step("Аутентификация пользователя")
    public LoginPom clickEntranceAnchor() {
        entranceAnchor.click();

        LoginPom loginPom = Selenide.page(LoginPom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> loginPom.isDisplayed());

        return loginPom;
    }

    public boolean isNameFieldDisplayed() {
        return Selenide.element(nameField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isEmailFieldDisplayed() {
        return Selenide.element(emailField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isPasswordFieldDisplayed() {
        return Selenide.element(passwordField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isRegisterButtonDisplayed() {
        return Selenide.element(registerButton).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isRegisterButtonClickable() {
        return Selenide.element(registerButton).shouldBe(Condition.enabled).isEnabled();
    }

    public boolean isEntranceAnchorDisplayed() {
        return Selenide.element(entranceAnchor).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isEntranceAnchorClickable() {
        return Selenide.element(entranceAnchor).shouldBe(Condition.enabled).isEnabled();
    }

    public boolean isDisplayed() {
        return Selenide.element(registerButton).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(emailField).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(passwordField).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(pageTitle).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(nameField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isPasswordFiledValid() {
        return !Selenide.$(By.className("input_type_password")).has(Condition.cssClass("input_status_error"));
    }

    public String getPageTitle() {
        return pageTitle.getText();
    }

    @Override
    public String toString() {
        return "RegisterPom{" +
               "emailField=" + getPassword() +
               ", passwordFiled=" + getEmail() +
               ", nameField=" + getName() +
               '}';
    }
}
