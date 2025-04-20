package kz.yandex.practicum.qa.sb.pom.auth;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import kz.yandex.practicum.qa.sb.pom.Layout;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordPom extends Layout {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/h2")
    SelenideElement pageTitle;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/fieldset/div/div/input")
    SelenideElement emailField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/form/button")
    SelenideElement resetButton;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/div/div/p/a")
    SelenideElement entranceAnchor;

    public ResetPasswordPom() {
        super();
    }

    public void clickResetButton() {
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

    public boolean isEntranceAnchorDisplayed() {
        return Selenide.element(entranceAnchor).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isEntranceAnchorClickable() {
        return Selenide.element(entranceAnchor).shouldBe(Condition.enabled).isEnabled();
    }

    public boolean isDisplayed() {
        return isEmailFieldDisplayed() && isResetButtonDisplayed() && isEntranceAnchorDisplayed();
    }


    public LoginPom clickEntranceAnchor() {
        entranceAnchor.click();

        LoginPom loginPom = Selenide.page(LoginPom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> loginPom.isDisplayed());

        return loginPom;
    }
}
