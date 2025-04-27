package kz.yandex.practicum.qa.sb.auth;

import com.codeborne.selenide.Selenide;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.main.HeaderPom;
import kz.yandex.practicum.qa.sb.pom.main.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.pom.auth.RegisterPom;
import kz.yandex.practicum.qa.sb.pom.auth.ResetPasswordPom;
import kz.yandex.practicum.qa.sb.rest.common.ApiException;
import kz.yandex.practicum.qa.sb.rest.common.Constants;
import kz.yandex.practicum.qa.sb.rest.user.User;
import kz.yandex.practicum.qa.sb.rest.user.UserRestClient;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Задание 3: веб-приложение
 * Вход
 * Проверь:
 *  вход по кнопке «Войти в аккаунт» на главной,
 *  вход через кнопку «Личный кабинет»,
 *  вход через кнопку в форме регистрации,
 *  вход через кнопку в форме восстановления пароля.
 * */
public class EntranceTest {

    private User registeredUser;

    // создаем пользователя для теста аутентификации
    @BeforeEach
    void beforeEach() {
        
        SelenideBrowserConfigurator.configure();
        
        assertDoesNotThrow(() -> {
            registeredUser = UserRestClient.create(new User()
                    .setName(FAKER.name().username())
                    .setEmail(FAKER.internet().emailAddress())
                    .setPassword(FAKER.internet().password()));
        });
    }

    // удаляем пользователя после тестов
    @AfterEach
    void afterEach() {
        if(registeredUser != null) {
            assertDoesNotThrow(() -> {
                UserRestClient.delete(registeredUser);
            });
            // проверяем что пользователь действительно удален
            ApiException e = assertThrows(ApiException.class, () -> {
                UserRestClient.getInfo(registeredUser.getAccessToken());
            });
            assertEquals(HttpStatus.SC_NOT_FOUND, e.getStatus());
            assertEquals(Constants.ERROR_MESSAGE_USER_NOT_FOUND, e.getMessage());   
        }
    }

    @AfterAll
    public static void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    @DisplayName("тест входа по кнопке «Войти в аккаунт» на главной")
    public void testEntranceFromHomePage() {
        AtomicReference<HomePom> home = new AtomicReference<>(Selenide.open(Constants.STELLAR_BURGERS_BASE_URL, HomePom.class));
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> home.get().isDisplayed());

        assertTrue(home.get().isEntranceButtonDisplayed() && home.get().isEntranceButtonClickable());

        LoginPom login = home.get().clickEntranceButton();

        login.setEmail(registeredUser.getEmail());
        login.setPassword(registeredUser.getPassword());

        home.set(login.clickEntranceButton());

        assertTrue(home.get().isOrderButtonDisplayed() && home.get().isOrderButtonClickable());
    }

    // В данном тесте так же неявно тестируется
    // Переход в личный кабинет (Проверь переход по клику на «Личный кабинет».)
    @Test
    @DisplayName("тест входа через кнопку «Личный кабинет»")
    public void testEntranceUsingAccountButton() {
        AtomicReference<HomePom> home = new AtomicReference<>(Selenide.open(Constants.STELLAR_BURGERS_BASE_URL, HomePom.class));
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> home.get().isDisplayed());

        HeaderPom header = home.get().getHeader();
        LoginPom login = header.clickPersonalAccountAnchor(LoginPom.class);
        login.setEmail(registeredUser.getEmail());
        login.setPassword(registeredUser.getPassword());

        home.set(login.clickEntranceButton());

        assertTrue(home.get().isOrderButtonDisplayed() && home.get().isOrderButtonClickable());
    }

    @Test
    @DisplayName("тест входа через кнопку в форме регистрации")
    public void testEntranceFromRegistrationPage() {
        RegisterPom register = Selenide.open(Constants.STELLAR_BURGERS_REGISTER_URL, RegisterPom.class);
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> register.isDisplayed());

        LoginPom login = register.clickEntranceAnchor();
        login.setEmail(registeredUser.getEmail());
        login.setPassword(registeredUser.getPassword());

        HomePom home = login.clickEntranceButton();

        assertTrue(home.isOrderButtonDisplayed() && home.isOrderButtonClickable());
    }

    @Test
    @DisplayName("тест входа через кнопку в форме восстановления пароля")
    public void testEntranceFromResetPasswordPage() {
        ResetPasswordPom resetPasswordPom = Selenide.open(Constants.STELLAR_BURGERS_FORGOT_PASSWORD_URL, ResetPasswordPom.class);
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> resetPasswordPom.isDisplayed());

        LoginPom login = resetPasswordPom.clickEntranceAnchor();
        login.setEmail(registeredUser.getEmail());
        login.setPassword(registeredUser.getPassword());

        HomePom home = login.clickEntranceButton();

        assertTrue(home.isOrderButtonDisplayed() && home.isOrderButtonClickable());
    }
}
