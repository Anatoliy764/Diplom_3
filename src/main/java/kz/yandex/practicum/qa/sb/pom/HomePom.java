package kz.yandex.practicum.qa.sb.pom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class HomePom extends Layout {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/section[2]/div/button")
    SelenideElement dynamicButton;

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

    public LoginPom enter() {
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

    public void order() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
