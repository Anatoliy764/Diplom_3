package kz.yandex.practicum.qa.sb.auth;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import kz.yandex.practicum.qa.sb.FakerInstance;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.HeaderPom;
import kz.yandex.practicum.qa.sb.pom.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.pom.auth.RegisterPom;
import kz.yandex.practicum.qa.sb.pom.auth.ResetPasswordPom;
import kz.yandex.practicum.qa.sb.rest.common.ApiException;
import kz.yandex.practicum.qa.sb.rest.user.User;
import kz.yandex.practicum.qa.sb.rest.user.UserRestClient;
import org.junit.*;
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

    private static final AtomicReference<User> REGISTERED_USER = new AtomicReference<>();

    public EntranceTest(String browser, MutableCapabilities browserOptions) {
        SelenideBrowserConfigurator.configure(browser, browserOptions);
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                {BrowserType.CHROME, new ChromeOptions().addArguments("--incognito")},
                {"yandex", new ChromeOptions().addArguments("--incognito")}
        };
    }

    // создаем пользователя для теста аутентификации
    @BeforeClass
    public static void setup() throws ApiException {
        try {
            REGISTERED_USER.set(UserRestClient.create(new User()
                    .setName(FAKER.name().username())
                    .setEmail(FAKER.internet().emailAddress())
                    .setPassword(FAKER.internet().password())));
        } catch (ApiException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // удаляем пользователя после тестов
    @AfterClass
    public static void teardown() throws ApiException {
        try {
            UserRestClient.delete(REGISTERED_USER.get());
            // проверяем что пользователь действительно удален
            ApiException apiException = Assert.assertThrows(ApiException.class, () -> {
                UserRestClient.getInfo(REGISTERED_USER.get().getAccessToken());
            });
            Assert.assertEquals("User not found", apiException.getMessage());
        } catch (ApiException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }
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

        login.setEmail(REGISTERED_USER.get().getEmail());
        login.setPassword(REGISTERED_USER.get().getPassword());

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
        login.setEmail(REGISTERED_USER.get().getEmail());
        login.setPassword(REGISTERED_USER.get().getPassword());

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
        login.setEmail(REGISTERED_USER.get().getEmail());
        login.setPassword(REGISTERED_USER.get().getPassword());

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
        login.setEmail(REGISTERED_USER.get().getEmail());
        login.setPassword(REGISTERED_USER.get().getPassword());

        HomePom home = login.clickEntranceButton();

        assertTrue(home.isOrderButtonDisplayed() && home.isOrderButtonClickable());
    }

}
