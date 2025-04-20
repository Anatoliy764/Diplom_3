package kz.yandex.practicum.qa.sb.account;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.AccountPom;
import kz.yandex.practicum.qa.sb.pom.ConstructorPom;
import kz.yandex.practicum.qa.sb.pom.HeaderPom;
import kz.yandex.practicum.qa.sb.pom.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.pom.auth.RegisterPom;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;

/**
 * Задание 3: веб-приложение
 * Переход из личного кабинета в конструктор
 * Проверь переход по клику на «Конструктор» и на логотип Stellar Burgers.
 * */
@RunWith(Parameterized.class)
public class GoToConstructorFromAccountTest {

    private static final AtomicReference<String> REGISTERED_NAME = new AtomicReference<>();
    private static final AtomicReference<String> REGISTERED_EMAIL = new AtomicReference<>();
    private static final AtomicReference<String> REGISTERED_PASSWORD = new AtomicReference<>();

    private static AccountPom account;

    public GoToConstructorFromAccountTest(String browser, MutableCapabilities browserOptions) {
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
    }

    @Before
    public void beforeTest() {
        LoginPom loginPom = Selenide.open("https://stellarburgers.nomoreparties.site/login", LoginPom.class);

        loginPom.setEmail(REGISTERED_EMAIL.get());
        loginPom.setPassword(REGISTERED_PASSWORD.get());

        HomePom home = loginPom.clickEntranceButton();

        HeaderPom header = home.getHeader();

        account = header.clickPersonalAccountAnchor(AccountPom.class);
    }

    @After
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    @DisplayName("Переход из личного кабинета в конструктор")
    @Description("переход по клику на «Конструктор» и на логотип Stellar Burgers.")
    public void goToConstructorTest() {
        HeaderPom header = account.getHeader();

        ConstructorPom constructorPom = header.clickConstructorAnchor();

        Assert.assertEquals("Соберите бургер", constructorPom.getHeading());
    }
}
