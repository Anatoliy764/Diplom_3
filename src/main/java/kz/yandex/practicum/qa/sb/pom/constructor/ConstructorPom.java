package kz.yandex.practicum.qa.sb.pom.constructor;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConstructorPom {

    @FindBy(how = How.XPATH, using = "//h1[text()='Соберите бургер']")
    SelenideElement heading;

    @FindBy(how = How.XPATH, using = "//div[span[text()='Булки']]")
    SelenideElement bunsTab;

    @FindBy(how = How.XPATH, using = "//div[span[text()='Соусы']]")
    SelenideElement saucesTab;

    @FindBy(how = How.XPATH, using = "//div[span[text()='Начинки']]")
    SelenideElement fillingsTab;

    @FindBy(how = How.CSS, using = ".BurgerIngredients_ingredients__menuContainer__Xu3Mo")
    SelenideElement scrollableDiv;

    @Step("Клик на одну из вкладок конструктора")
    public void clickTab(Tab tab) {
        switch (tab) {
            case BUNS: clickBunsTab(); break;
            case SAUCES: clickSaucesTab(); break;
            case FILLINGS: clickFillingsTab(); break;
        }
    }

    @Step("Клик на вкладку \"Булки\"")
    public void clickBunsTab() {
        bunsTab.click();
        Selenide.Wait().withTimeout(Duration.ofSeconds(1));
    }

    @Step("Клик на вкладку \"Соусы\"")
    public void clickSaucesTab() {
        saucesTab.click();
        Selenide.Wait().withTimeout(Duration.ofSeconds(1));
    }

    @Step("Клик на вкладку \"Начинки\"")
    public void clickFillingsTab() {
        fillingsTab.click();
        Selenide.Wait().withTimeout(Duration.ofSeconds(1));
    }

    public Tab getCurrentTab() {
        return Tab.valueOfLabel(Selenide.$(By.className("tab_tab_type_current__2BEPc")).getText());
    }

    public boolean isDisplayed() {
        return isHeadingDisplayed() &&
               isBunsTabDisplayed() &&
               isSaucesTabDisplayed() &&
               isFillingsTabDisplayed();
    }

    public boolean isHeadingDisplayed() {
        return Selenide.element(heading).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isBunsTabDisplayed() {
        return Selenide.element(bunsTab).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isSaucesTabDisplayed() {
        return Selenide.element(saucesTab).shouldBe(Condition.visible).isDisplayed();
    }

    public boolean isFillingsTabDisplayed() {
        return Selenide.element(fillingsTab).shouldBe(Condition.visible).isDisplayed();
    }

    public String getHeading() {
        return heading.getText();
    }

    public double getScrollPosition() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) Selenide.webdriver().object();
        Number currentPosition = (Number) jsExecutor.executeScript("return arguments[0].scrollTop;", scrollableDiv);
        return currentPosition.doubleValue();
    }
}
