package kz.yandex.practicum.qa.sb.auth;

import com.codeborne.selenide.Selenide;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.pom.auth.RegisterPom;
import kz.yandex.practicum.qa.sb.rest.common.ApiException;
import kz.yandex.practicum.qa.sb.rest.common.Constants;
import kz.yandex.practicum.qa.sb.rest.user.User;
import kz.yandex.practicum.qa.sb.rest.user.UserRestClient;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static kz.yandex.practicum.qa.sb.AssertionFailMessage.*;
import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Задание 3: веб-приложение
 * Регистрация
 * Проверь:
 *  Успешную регистрацию.
 *  Ошибку для некорректного пароля. Минимальный пароль — шесть символов.
 * */
class RegisterTest {

    private RegisterPom registerPom;

    private User registeredUser;

    @BeforeEach
    void beforeEach() {

        SelenideBrowserConfigurator.configure();

        registerPom = Selenide.open(Constants.STELLAR_BURGERS_REGISTER_URL, RegisterPom.class);
        Selenide.Wait().withTimeout(Duration.ofSeconds(5)).until(obj -> registerPom.isDisplayed());
        assertEquals("Регистрация", registerPom.getPageTitle());
    }

    @AfterEach
    public void tearDown() {

        Selenide.closeWebDriver();

        if(registeredUser != null) {
            assertDoesNotThrow(() -> {
                UserRestClient.delete(registeredUser);
            });
            // проверяем что пользователь действительно удален
            ApiException apiException = assertThrows(ApiException.class, () -> {
                UserRestClient.getInfo(registeredUser.getAccessToken());
            });
            assertEquals("User not found", apiException.getMessage());
        }
    }

    @Test
    @DisplayName("тест регистрации с валидными данными")
    void testRegister() {
        // заполняем форму
        String email = FAKER.internet().emailAddress();
        String username = FAKER.name().username();
        String password = FAKER.internet().password();

        registerPom.setEmail(email);
        registerPom.setName(username);
        registerPom.setPassword(password);

        // проверяем что кнопка "зарегистрироваться" кликабельна
        assertTrue(registerPom.isRegisterButtonClickable(), REGISTER_BUTTON_UNCLICKABLE);

        // регистрируем пользователя
        LoginPom loginPom = registerPom.clickRegisterButton();

        // после регистрации открывается форма для входа. Проверяем что форма открылась.
        assertTrue(loginPom.isDisplayed(), "Форма входа не отобразилась");

        // пытаемся аутентифицироваться посредством REST API,
        // тем самым проверяем что пользователь действительно зарегистрирован.
        assertDoesNotThrow(() -> {
            registeredUser = UserRestClient.login(email, password);
            assertNotNull(registeredUser, "Не удалось аутентифицировать пользователя посредством REST API");
        });
    }

    // password min length = 6
    @Test
    @DisplayName("тест регистрации с невалидным паролем")
    void testRegisterWithInvalidPassword() {
        // заполняем форму
        String email = FAKER.internet().emailAddress();
        String username = FAKER.name().username();
        String invalidPassword = "qwert";

        registerPom.setEmail(email);
        registerPom.setName(username);
        registerPom.setPassword(invalidPassword);

        // проверяем что кнопка "зарегистрироваться" кликабельна
        assertTrue(registerPom.isRegisterButtonClickable(), REGISTER_BUTTON_UNCLICKABLE);

        // регистрируем пользователя
        assertThrows(com.codeborne.selenide.ex.ElementNotFound.class, () -> registerPom.clickRegisterButton());

        // проверяем что поле "Пароль" не валидно и содержит ошибку
        assertFalse(registerPom.isPasswordFiledValid(), "Пароль менее 6 не должен быть валиден");

        // пытаемся аутентифицироваться посредством REST API,
        // тем самым проверяем что пользователь действительно не зарегистрирован.
        ApiException e = assertThrows(ApiException.class, () -> {
            UserRestClient.login(email, invalidPassword);
        });
        assertEquals(HttpStatus.SC_UNAUTHORIZED, e.getStatus(), HTTP_STATUS_CODE_MISMATCH);
        assertEquals(Constants.ERROR_MESSAGE_INVALID_CREDENTIALS, e.getMessage(), ERROR_MESSAGE_MISMATCH);
    }
}