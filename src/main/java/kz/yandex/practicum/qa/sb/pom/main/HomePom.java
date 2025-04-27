package kz.yandex.practicum.qa.sb.pom.main;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomePom extends Layout {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/section[2]/div/button")
    SelenideElement dynamicButton;

    public HomePom() {
        super();
    }

    public boolean isDisplayed() {
        return Selenide.element(dynamicButton).shouldBe(Condition.visible).isDisplayed() &&
               Selenide.element(dynamicButton).shouldBe(Condition.visible).isDisplayed();
    }

    protected boolean isDynamicButtonDisplayed() {
        return Selenide.element(dynamicButton).shouldBe(Condition.visible).isDisplayed();
    }

    protected boolean isDynamicButtonClickable() {
        return Selenide.element(dynamicButton).shouldBe(Condition.enabled).isEnabled();
    }

    public boolean isEntranceButtonDisplayed() {
        return isDynamicButtonDisplayed() && Objects.equals("Войти в аккаунт", dynamicButton.getText());
    }

    public boolean isEntranceButtonClickable() {
        return isDynamicButtonClickable();
    }

    public boolean isOrderButtonDisplayed() {
        return isDynamicButtonDisplayed() && Objects.equals("Оформить заказ", dynamicButton.getText());
    }

    public boolean isOrderButtonClickable() {
        return isDynamicButtonClickable();
    }

    @Step("Аутентификация пользователя")
    public LoginPom clickEntranceButton() {
        AtomicReference<LoginPom> loginPom = new AtomicReference<>();
        if(isEntranceButtonDisplayed()) {
            dynamicButton.click();
            loginPom.set(Selenide.page(LoginPom.class));
        } else {
            throw new IllegalStateException("You already logged in");
        }
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> loginPom.get().isDisplayed());

        return loginPom.get();
    }
}
