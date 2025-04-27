package kz.yandex.practicum.qa.sb.account;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import kz.yandex.practicum.qa.sb.AssertionFailMessage;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.account.AccountPom;
import kz.yandex.practicum.qa.sb.pom.account.ProfilePom;
import kz.yandex.practicum.qa.sb.pom.main.HeaderPom;
import kz.yandex.practicum.qa.sb.pom.main.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.rest.common.ApiException;
import kz.yandex.practicum.qa.sb.rest.common.Constants;
import kz.yandex.practicum.qa.sb.rest.user.User;
import kz.yandex.practicum.qa.sb.rest.user.UserRestClient;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Задание 3: веб-приложение
 * Переход в личный кабинет
 * Проверь переход по клику на «Личный кабинет».
 * */
class PersonalAccountTest {

    private User registeredUser;

    private static HomePom home;

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

        LoginPom loginPom = Selenide.open("https://stellarburgers.nomoreparties.site/login", LoginPom.class);

        loginPom.setEmail(registeredUser.getEmail());
        loginPom.setPassword(registeredUser.getPassword());

        home = loginPom.clickEntranceButton();
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
    @DisplayName("Переход в личный кабинет")
    @Description("переход по клику на «Личный кабинет».")
    public void testLinkToPersonalAccount() {

        HeaderPom header = home.getHeader();

        AccountPom accountPom = header.clickPersonalAccountAnchor(AccountPom.class);

        assertTrue(accountPom.isDisplayed(), "Страница личного кабинета не отобразилась");

        ProfilePom profile = accountPom.clickProfileAnchor();

        assertTrue(profile.isDisplayed(), "Страница профиля не отобразилась");

        assertEquals(registeredUser.getEmail(), profile.getEmail(), AssertionFailMessage.USER_EMAIL_MISMATCH);
    }
}
