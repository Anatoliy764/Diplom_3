package kz.yandex.practicum.qa.sb.pom.auth;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import kz.yandex.practicum.qa.sb.pom.main.Layout;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordPom extends Layout {

    @FindBy(how = How.XPATH, using = "//label[text()='Email']/following-sibling::input")
    SelenideElement emailField;

    @FindBy(how = How.XPATH, using = "//button[text()='Восстановить']")
    SelenideElement resetButton;

    @FindBy(how = How.XPATH, using = "//a[text()='Войти']")
    SelenideElement entranceAnchor;

    public ResetPasswordPom() {
        super();
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

    public boolean isResetButtonDisplayed() {
        return Selenide.element(resetButton).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isEntranceAnchorDisplayed() {
        return Selenide.element(entranceAnchor).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isDisplayed() {
        return isEmailFieldDisplayed() && isResetButtonDisplayed() && isEntranceAnchorDisplayed();
    }

    @Step("Аутентификация пользователя")
    public LoginPom clickEntranceAnchor() {
        entranceAnchor.click();

        LoginPom loginPom = Selenide.page(LoginPom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> loginPom.isDisplayed());

        return loginPom;
    }
}
