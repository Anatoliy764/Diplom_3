package kz.yandex.practicum.qa.sb.account;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.account.AccountPom;
import kz.yandex.practicum.qa.sb.pom.constructor.ConstructorPom;
import kz.yandex.practicum.qa.sb.pom.main.HeaderPom;
import kz.yandex.practicum.qa.sb.pom.main.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.rest.common.ApiException;
import kz.yandex.practicum.qa.sb.rest.common.Constants;
import kz.yandex.practicum.qa.sb.rest.user.User;
import kz.yandex.practicum.qa.sb.rest.user.UserRestClient;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Задание 3: веб-приложение
 * Переход из личного кабинета в конструктор
 * Проверь переход по клику на «Конструктор» и на логотип Stellar Burgers.
 * */
class GoToConstructorFromAccountTest {

    private User registeredUser;

    private static AccountPom account;

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

        LoginPom loginPom = Selenide.open(Constants.STELLAR_BURGERS_LOGIN_URL, LoginPom.class);

        loginPom.setEmail(registeredUser.getEmail());
        loginPom.setPassword(registeredUser.getPassword());

        HomePom home = loginPom.clickEntranceButton();

        HeaderPom header = home.getHeader();

        account = header.clickPersonalAccountAnchor(AccountPom.class);
    }

    // удаляем пользователя после тестов
    @AfterEach
    void afterEach() {
        Selenide.closeWebDriver();

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

    @Test
    @DisplayName("Переход из личного кабинета в конструктор")
    @Description("переход по клику на «Конструктор» и на логотип Stellar Burgers.")
    public void goToConstructorTest() {
        HeaderPom header = account.getHeader();

        ConstructorPom constructorPom = header.clickConstructorAnchor();

        assertEquals("Соберите бургер", constructorPom.getHeading());
    }
}
