package kz.yandex.practicum.qa.sb.account;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.AccountPom;
import kz.yandex.practicum.qa.sb.pom.HeaderPom;
import kz.yandex.practicum.qa.sb.pom.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.pom.auth.RegisterPom;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Задание 3: веб-приложение
 * Переход в личный кабинет
 * Проверь переход по клику на «Личный кабинет».
 * */
@Slf4j
@RunWith(Parameterized.class)
public class PersonalAccountTest {

    private static final AtomicReference<String> REGISTERED_NAME = new AtomicReference<>();
    private static final AtomicReference<String> REGISTERED_EMAIL = new AtomicReference<>();
    private static final AtomicReference<String> REGISTERED_PASSWORD = new AtomicReference<>();

    private static HomePom home;

    public PersonalAccountTest(String browser, MutableCapabilities browserOptions) {
        SelenideBrowserConfigurator.configure(browser, browserOptions);
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                {BrowserType.CHROME, new ChromeOptions().addArguments("--incognito")},
                {"yandex", new ChromeOptions().addArguments("--incognito")}
        };
    }

    @BeforeClass
    public static void setup() {

        String registeredEmail = System.getenv("REGISTERED_EMAIL");
        String registeredPassword = System.getenv("REGISTERED_PASSWORD");

        if((registeredEmail == null || registeredEmail.isBlank()) && (registeredPassword == null || registeredPassword.isBlank())) {
            // register new user
            RegisterPom registerPom = Selenide.open("https://stellarburgers.nomoreparties.site/register", RegisterPom.class);
            Selenide.Wait().withTimeout(Duration.ofSeconds(5)).until(obj -> registerPom.isDisplayed());

            REGISTERED_NAME.set(FAKER.name().username());
            REGISTERED_EMAIL.set(FAKER.internet().emailAddress());
            REGISTERED_PASSWORD.set(FAKER.internet().password());

            registerPom.setName(REGISTERED_NAME.get());
            registerPom.setEmail(REGISTERED_EMAIL.get());
            registerPom.setPassword(REGISTERED_PASSWORD.get());

            registerPom.clickRegisterButton();
        } else {
            REGISTERED_EMAIL.set(registeredEmail);
            REGISTERED_PASSWORD.set(registeredPassword);
        }

        System.out.printf("User for entrance\nname: %s\nemail: %s\npassword: %s\n", REGISTERED_NAME, REGISTERED_EMAIL, REGISTERED_PASSWORD);

        LoginPom loginPom = Selenide.open("https://stellarburgers.nomoreparties.site/login", LoginPom.class);

        loginPom.setEmail(REGISTERED_EMAIL.get());
        loginPom.setPassword(REGISTERED_PASSWORD.get());

        home = loginPom.clickEntranceButton();
    }

    @After
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    @DisplayName("Переход в личный кабинет")
    @Description("переход по клику на «Личный кабинет».")
    public void testLinkToPersonalAccount() {

        HeaderPom header = home.getHeader();

        AccountPom accountPom = header.clickPersonalAccountAnchor(AccountPom.class);

        assertTrue(accountPom.isDisplayed());

        AccountPom.Profile profile = accountPom.clickProfileAnchor();

        assertTrue(profile.isDisplayed());

        assertEquals(REGISTERED_EMAIL.get(), profile.getEmail());
    }

}
