package kz.yandex.practicum.qa.sb.auth;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.junit4.DisplayName;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.pom.auth.RegisterPom;
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
import java.util.LinkedList;
import java.util.List;

import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;
import static org.junit.Assert.*;

/**
 * Задание 3: веб-приложение
 * Регистрация
 * Проверь:
 *  Успешную регистрацию.
 *  Ошибку для некорректного пароля. Минимальный пароль — шесть символов.
 * */
@RunWith(Parameterized.class)
public class RegisterTest {

    private static final List<User> REGISTERED_USERS = new LinkedList<>();

    private RegisterPom registerPom;

    public RegisterTest(String browser, MutableCapabilities browserOptions) {
        SelenideBrowserConfigurator.configure(browser, browserOptions);
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                {BrowserType.CHROME, new ChromeOptions()},
                {"yandex", new ChromeOptions()}
        };
    }

    @Before
    public void beforeEach() {
        registerPom = Selenide.open("https://stellarburgers.nomoreparties.site/register", RegisterPom.class);
        Selenide.Wait().withTimeout(Duration.ofSeconds(5)).until(obj -> registerPom.isDisplayed());
        assertEquals("Регистрация", registerPom.getPageTitle());
    }

    @After
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    // удаляем пользователя после тестов
    @AfterClass
    public static void teardown() throws ApiException {
        for (User registeredUser : REGISTERED_USERS) {
            try {
                UserRestClient.delete(registeredUser);
                // проверяем что пользователь действительно удален
                ApiException apiException = Assert.assertThrows(ApiException.class, () -> {
                    UserRestClient.getInfo(registeredUser.getAccessToken());
                });
                Assert.assertEquals("User not found", apiException.getMessage());
            } catch (ApiException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }

    @Test
    @DisplayName("тест регистрации с валидными данными")
    public void testRegister() {
        // заполняем форму
        String email = FAKER.internet().emailAddress();
        String username = FAKER.name().username();
        String password = FAKER.internet().password();

        registerPom.setEmail(email);
        registerPom.setName(username);
        registerPom.setPassword(password);

        // проверяем что кнопка "зарегистрироваться" кликабельна
        assertTrue(registerPom.isRegisterButtonClickable());

        // регистрируем пользователя
        LoginPom loginPom = registerPom.clickRegisterButton();

        // после регистрации открывается форма для входа. Проверяем что форма открылась.
        assertTrue(loginPom.isDisplayed());
        assertEquals("Вход", loginPom.getPageTitle());

        // пытаемся получить зарегистрированного пользователя посредством REST API,
        // тем самым проверяем что пользователь действительно зарегистрирован.
        try {
            User user = UserRestClient.login(email, password);
            REGISTERED_USERS.add(user);
            User registeredUser = UserRestClient.getInfo(user.getAccessToken());
            assertNotNull(registeredUser);
            assertEquals(username, registeredUser.getName());
            assertEquals(email, registeredUser.getEmail());
        } catch (ApiException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // password min length = 6
    @Test
    @DisplayName("тест регистрации с невалидным паролем")
    public void testRegisterWithInvalidPassword() {
        // заполняем форму
        String email = FAKER.internet().emailAddress();
        String username = FAKER.name().username();
        String invalidPassword = "qwert";

        registerPom.setEmail(email);
        registerPom.setName(username);
        registerPom.setPassword(invalidPassword);

        // проверяем что кнопка "зарегистрироваться" кликабельна
        assertTrue(registerPom.isRegisterButtonClickable());

        // регистрируем пользователя
        registerPom.clickRegisterButton();

        // проверяем что поле "Пароль" не валидно и содержит ошибку
        assertFalse(registerPom.isPasswordFiledValid());

        // пытаемся аутентифицироваться посредством REST API,
        // тем самым проверяем что пользователь действительно не зарегистрирован.
        ApiException apiException = assertThrows(ApiException.class, () -> {
            UserRestClient.login(email, invalidPassword);
        });
        assertEquals("email or password are incorrect", apiException.getMessage());
    }
}