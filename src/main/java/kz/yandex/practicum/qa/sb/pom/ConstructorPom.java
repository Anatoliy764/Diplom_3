package kz.yandex.practicum.qa.sb.pom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.Arrays;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConstructorPom {

    private static final String BUNS_XPATH = "//*[@id=\"root\"]/div/main/section[1]/div[1]/div[1]";
    private static final String SAUCES_XPATH = "//*[@id=\"root\"]/div/main/section[1]/div[1]/div[2]";
    private static final String FILLINGS_XPATH = "//*[@id=\"root\"]/div/main/section[1]/div[1]/div[3]";

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/main/section[1]/h1")
    SelenideElement heading;

    @FindBy(how = How.XPATH, using = BUNS_XPATH)
    SelenideElement bunsTab;

    @FindBy(how = How.XPATH, using = SAUCES_XPATH)
    SelenideElement saucesTab;

    @FindBy(how = How.XPATH, using = FILLINGS_XPATH)
    SelenideElement fillingsTab;

    public void clickTab(Tab tab) {
        switch (tab) {
            case BUNS: clickBunsTab(); break;
            case SAUCES: clickSaucesTab(); break;
            case FILLINGS: clickFillingsTab(); break;
        }
    }

    public void clickBunsTab() {
        bunsTab.click();
        Selenide.sleep(1000);
    }

    public void clickSaucesTab() {
        saucesTab.click();
        Selenide.sleep(1000);
    }

    public void clickFillingsTab() {
        fillingsTab.click();
        Selenide.sleep(1000);
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

    @Getter
    @RequiredArgsConstructor
    public enum Tab {
        BUNS("Булки"),
        SAUCES("Соусы"),
        FILLINGS("Начинки");

        private final String label;

        public static Tab valueOfLabel(String label) {
            return Arrays.stream(values()).filter(t -> t.getLabel().equals(label)).findFirst().orElse(null);
        }
    }
}
