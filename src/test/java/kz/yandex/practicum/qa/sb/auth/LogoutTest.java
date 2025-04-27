package kz.yandex.practicum.qa.sb.auth;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.account.AccountPom;
import kz.yandex.practicum.qa.sb.pom.main.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.rest.common.ApiException;
import kz.yandex.practicum.qa.sb.rest.common.Constants;
import kz.yandex.practicum.qa.sb.rest.user.User;
import kz.yandex.practicum.qa.sb.rest.user.UserRestClient;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Задание 3: веб-приложение
 * Выход из аккаунта
 * Проверь выход по кнопке «Выйти» в личном кабинете.
 * */
@Epic("Выход из аккаунта")
@Feature("Проверка выхода по кнопке «Выйти» в личном кабинете.")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogoutTest {

    AccountPom accountPom;

    User registeredUser;

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

        HomePom homePom = loginPom.clickEntranceButton();

        accountPom = homePom.getHeader().clickPersonalAccountAnchor(AccountPom.class);
    }

    // удаляем пользователя после тестов
    @AfterEach
    void afterEach() {

        Selenide.closeWebDriver();

        if(registeredUser != null) {
            assertDoesNotThrow(() -> UserRestClient.delete(registeredUser));

            // проверяем что пользователь действительно удален
            ApiException e = assertThrows(ApiException.class, () -> {
                UserRestClient.getInfo(registeredUser.getAccessToken());
            });
            assertEquals(HttpStatus.SC_NOT_FOUND, e.getStatus());
            assertEquals(Constants.ERROR_MESSAGE_USER_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    @DisplayName("Выход из аккаунта")
    @Story("Выход пользователя из системы по кнопке «Выйти» в личном кабинете")
    public void testLogout() {
        LoginPom loginPom = accountPom.clickLogoutAnchor();

        assertTrue(loginPom.isDisplayed());
    }
}
