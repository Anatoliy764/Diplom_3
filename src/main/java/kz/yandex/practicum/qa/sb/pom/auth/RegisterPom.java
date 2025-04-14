package kz.yandex.practicum.qa.sb.pom.auth;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import kz.yandex.practicum.qa.sb.pom.HeaderPom;
import kz.yandex.practicum.qa.sb.pom.Layout;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterPom extends Layout {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/h2")
    SelenideElement pageTitle;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/fieldset[1]/div/div/input")
    SelenideElement nameField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/fieldset[2]/div/div/input")
    SelenideElement emailField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/fieldset[3]/div/div/input")
    SelenideElement passwordField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/button")
    SelenideElement registerButton;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/div/p/a")
    SelenideElement entranceAnchor;

    public RegisterPom() {
        this.headerPom = Selenide.page(HeaderPom.class);
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

    public LoginPom register() {
        registerButton.click();

        LoginPom loginPom = Selenide.page(LoginPom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> loginPom.isDisplayed());

        return loginPom;
    }

    public LoginPom enter() {
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
