package kz.yandex.practicum.qa.sb.constructor;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.constructor.ConstructorPom;
import kz.yandex.practicum.qa.sb.pom.constructor.Tab;
import kz.yandex.practicum.qa.sb.pom.main.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.rest.common.ApiException;
import kz.yandex.practicum.qa.sb.rest.common.Constants;
import kz.yandex.practicum.qa.sb.rest.user.User;
import kz.yandex.practicum.qa.sb.rest.user.UserRestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Задание 3: веб-приложение
 * Раздел «Конструктор»
 * Проверь, что работают переходы к разделам:
 * «Булки»,
 * «Соусы»,
 * «Начинки».
 */
@Slf4j
public class ConstructorTest {

    private static ConstructorPom constructorPom;

    private static User registeredUser;

    @BeforeAll
    static void beforeAll() {

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

        constructorPom = homePom.getHeader().clickConstructorAnchor();
    }

    // создаем пользователя для теста аутентификации
    @AfterAll
    static void afterAll() {
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

    private static Stream<Arguments> getTabs() {
        return Stream.of(
                Arguments.of(Tab.FILLINGS.getLabel()),
                Arguments.of(Tab.SAUCES.getLabel()),
                Arguments.of(Tab.BUNS.getLabel())
        );
    }

    // При клике на вкладку конструктора ползунок меняет свою позицию и отображает соответсвующий контент.
    // Проверять отображаемый контент нет смысла, т.к. при загрузке конструктора страница содержит весь контент,
    // а не подгружает его динамически в соответствии с выбранной вкладкой.
    // Иначе говоря нет смысла проверять отображается ли к примеру начинка Мясо бессмертных моллюсков Protostomia,
    // т.к. даже если текущая вкладка это "Булки", начинка находится на странице.
    // Поэтому проверяем что ползунок двигается при выборе вкладки.
    @ParameterizedTest(name = "Тест раздела \"Конструктор\" переход по вкладке: {0}")
    @MethodSource("getTabs")
    @Description("проверка работы перехода по вкладкам \"Булки\", \"Соусы\", \"Начинки\" ")
    public void testClickTab(String tabLabel) {

        Tab tab = Tab.valueOfLabel(tabLabel);

        double initialScrollPos = constructorPom.getScrollPosition();

        constructorPom.clickTab(tab);

        Tab currentTab = constructorPom.getCurrentTab();

        assertTrue(currentTab != null && Objects.equals(tab, currentTab));

        double currentScrollPos = constructorPom.getScrollPosition();

        assertNotEquals(initialScrollPos, currentScrollPos);
    }
}
