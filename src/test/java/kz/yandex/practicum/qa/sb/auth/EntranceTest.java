package kz.yandex.practicum.qa.sb.auth;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.HeaderPom;
import kz.yandex.practicum.qa.sb.pom.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.pom.auth.RegisterPom;
import kz.yandex.practicum.qa.sb.pom.auth.ResetPasswordPom;
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
import static org.junit.Assert.assertTrue;

/**
 * Задание 3: веб-приложение
 * Вход
 * Проверь:
 *  вход по кнопке «Войти в аккаунт» на главной,
 *  вход через кнопку «Личный кабинет»,
 *  вход через кнопку в форме регистрации,
 *  вход через кнопку в форме восстановления пароля.
 * */
@RunWith(Parameterized.class)
public class EntranceTest {

    private static final AtomicReference<String> REGISTERED_NAME = new AtomicReference<>();
    private static final AtomicReference<String> REGISTERED_EMAIL = new AtomicReference<>();
    private static final AtomicReference<String> REGISTERED_PASSWORD = new AtomicReference<>();

    public EntranceTest(String browser, MutableCapabilities browserOptions) {
        SelenideBrowserConfigurator.configure(browser, browserOptions);
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                {BrowserType.CHROME, new ChromeOptions().addArguments("--incognito")},
//                {"yandex", new ChromeOptions().addArguments("--incognito")}
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

    @After
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    @DisplayName("тест входа")
    @Description("вход по кнопке «Войти в аккаунт» на главной")
    public void testEntranceFromHomePage() {
        AtomicReference<HomePom> home = new AtomicReference<>(Selenide.open("https://stellarburgers.nomoreparties.site", HomePom.class));
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> home.get().isDisplayed());

        assertTrue(home.get().isEntranceButtonDisplayed() && home.get().isEntranceButtonClickable());

        LoginPom login = home.get().clickEntranceButton();

        login.setEmail(REGISTERED_EMAIL.get());
        login.setPassword(REGISTERED_PASSWORD.get());

        home.set(login.clickEntranceButton());

        assertTrue(home.get().isOrderButtonDisplayed() && home.get().isOrderButtonClickable());
    }

    // В данном тесте так же неявно тестируется
    // Переход в личный кабинет (Проверь переход по клику на «Личный кабинет».)
    @Test
    @DisplayName("тест входа")
    @Description("вход через кнопку «Личный кабинет»")
    public void testEntranceUsingAccountButton() {
        AtomicReference<HomePom> home = new AtomicReference<>(Selenide.open("https://stellarburgers.nomoreparties.site", HomePom.class));
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> home.get().isDisplayed());

        HeaderPom header = home.get().getHeader();
        LoginPom login = header.clickPersonalAccountAnchor(LoginPom.class);
        login.setEmail(REGISTERED_EMAIL.get());
        login.setPassword(REGISTERED_PASSWORD.get());

        home.set(login.clickEntranceButton());

        assertTrue(home.get().isOrderButtonDisplayed() && home.get().isOrderButtonClickable());
    }

    @Test
    @DisplayName("тест входа")
    @Description("вход через кнопку в форме регистрации")
    public void testEntranceFromRegistrationPage() {
        RegisterPom register = Selenide.open("https://stellarburgers.nomoreparties.site/register", RegisterPom.class);
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> register.isDisplayed());

        LoginPom login = register.clickEntranceAnchor();
        login.setEmail(REGISTERED_EMAIL.get());
        login.setPassword(REGISTERED_PASSWORD.get());

        HomePom home = login.clickEntranceButton();

        assertTrue(home.isOrderButtonDisplayed() && home.isOrderButtonClickable());
    }

    @Test
    @DisplayName("тест входа")
    @Description("вход через кнопку в форме восстановления пароля")
    public void testEntranceFromResetPasswordPage() {
        ResetPasswordPom resetPasswordPom = Selenide.open("https://stellarburgers.nomoreparties.site/forgot-password", ResetPasswordPom.class);
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> resetPasswordPom.isDisplayed());

        LoginPom login = resetPasswordPom.clickEntranceAnchor();
        login.setEmail(REGISTERED_EMAIL.get());
        login.setPassword(REGISTERED_PASSWORD.get());

        HomePom home = login.clickEntranceButton();

        assertTrue(home.isOrderButtonDisplayed() && home.isOrderButtonClickable());
    }

}
