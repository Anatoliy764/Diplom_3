package kz.yandex.practicum.qa.sb.pom.account;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfilePom {
    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/div/div/ul/li[1]/div/div/input")
    SelenideElement nameField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/div/div/ul/li[2]/div/div/input")
    SelenideElement emailField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/div/div/ul/li[3]/div/div/input")
    SelenideElement passwordField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/div/div/div/button[1]")
    SelenideElement cancelButton;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/div/div/div/button[2]")
    SelenideElement saveButton;

    public boolean isNameFieldDisplayed() {
        return Selenide.element(nameField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isEmailFieldDisplayed() {
        return Selenide.element(emailField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isPasswordFieldDisplayed() {
        return Selenide.element(passwordField).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isCancelButtonDisplayed() {
        return Selenide.element(cancelButton).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isSaveButtonDisplayed() {
        return Selenide.element(saveButton).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isDisplayed() {
        return isNameFieldDisplayed() &&
               isEmailFieldDisplayed() &&
               isPasswordFieldDisplayed() &&
               isCancelButtonDisplayed() &&
               isSaveButtonDisplayed();
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

}
