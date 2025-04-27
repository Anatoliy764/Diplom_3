package kz.yandex.practicum.qa.sb.pom.account;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import kz.yandex.practicum.qa.sb.pom.main.Layout;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountPom extends Layout {

    @FindBy(how = How.XPATH, using = "//a[text()='Профиль']")
    private SelenideElement profileAnchor;

    @FindBy(how = How.XPATH, using = "//button[text()='Выход']")
    private SelenideElement logoutAnchor;

    public AccountPom() {
        super();
    }

    public boolean isProfileAnchorDisplayed() {
        return Selenide.element(profileAnchor).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isLogoutAnchorDisplayed() {
        return Selenide.element(logoutAnchor).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isDisplayed() {
        return isProfileAnchorDisplayed() && isLogoutAnchorDisplayed();
    }

    @Step("Выход из системы")
    public LoginPom clickLogoutAnchor() {
        logoutAnchor.click();

        LoginPom loginPom = Selenide.page(LoginPom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5)).until(webDriver -> loginPom.isDisplayed());

        return loginPom;
    }

    @Step("Переход в личный кабинет")
    public ProfilePom clickProfileAnchor() {

        profileAnchor.click();

        ProfilePom profilePom = Selenide.page(ProfilePom.class);

        Selenide.Wait().withTimeout(Duration.ofSeconds(5)).until(webDriver -> profilePom.isDisplayed());

        return profilePom;
    }
}
