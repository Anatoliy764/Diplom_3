package kz.yandex.practicum.qa.sb.auth;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.junit4.DisplayName;
import kz.yandex.practicum.qa.sb.FakerInstance;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.pom.auth.RegisterPom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;

import java.time.Duration;

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

    @Test
    @DisplayName("тест регистрации с валидными данными")
    public void testRegister() {
        // fill form
        registerPom.setEmail(FAKER.internet().emailAddress());
        registerPom.setName(FAKER.name().username());
        registerPom.setPassword(FakerInstance.FAKER.internet().password());

        assertTrue(registerPom.isRegisterButtonClickable());

        LoginPom loginPom = registerPom.clickRegisterButton();

        // как проверить, что после регистрации пользователь реально создан и существует?
        // 1. попробовать войти, но это неявный тест входа.
        // 2. использовать REST API и попробовать получить информацию о пользователя, но нет доступа к токену.
        // т.к. описанные варианты выше не подходят, то отображение следующей формы за регистрацией (форма входа) считается успешной регистрацией

        assertTrue(loginPom.isDisplayed());
        assertEquals("Вход", loginPom.getPageTitle());
    }

    // password min length = 6
    @Test
    @DisplayName("тест регистрации с невалидным паролем")
    public void testRegisterWithInvalidPassword() {
        // fill form
        registerPom.setEmail(FAKER.internet().emailAddress());
        registerPom.setName(FAKER.name().username());
        registerPom.setPassword("qwert");

        assertTrue(registerPom.isRegisterButtonClickable());

        registerPom.clickRegisterButton();
        assertFalse(registerPom.isPasswordFiledValid());
    }
}