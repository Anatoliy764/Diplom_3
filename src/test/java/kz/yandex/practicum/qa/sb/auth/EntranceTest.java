package kz.yandex.practicum.qa.sb.auth;

import com.codeborne.selenide.Selenide;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import kz.yandex.practicum.qa.sb.FakerInstance;
import kz.yandex.practicum.qa.sb.pom.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.pom.auth.RegisterPom;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;

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
public class EntranceTest {

    private static final String REGISTERED_NAME = FAKER.name().username();
    private static final String REGISTERED_EMAIL = FAKER.internet().emailAddress();
    private static final String REGISTERED_PASSWORD = FAKER.internet().password();

    @BeforeClass
    public static void beforeAll() {
        System.out.printf("Registered\nname: %s\nemail: %s\npassword: %s\n", REGISTERED_NAME, REGISTERED_EMAIL, REGISTERED_PASSWORD);
        WebDriverManager.chromedriver().setup();

        RegisterPom registerPom = Selenide.open("https://stellarburgers.nomoreparties.site/register", RegisterPom.class);
        Selenide.Wait().withTimeout(Duration.ofSeconds(5)).until(obj -> registerPom.isDisplayed());

        registerPom.setName(REGISTERED_NAME);
        registerPom.setEmail(REGISTERED_EMAIL);
        registerPom.setPassword(REGISTERED_PASSWORD);

        registerPom.register();
    }

    @Test
    @DisplayName("тест входа")
    @Description("вход по кнопке «Войти в аккаунт» на главной")
    public void testEntranceOnHomePage() {
        HomePom home = Selenide.open("https://stellarburgers.nomoreparties.site", HomePom.class);
        HomePom finalHome = home;
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> finalHome.isDisplayed());

        assertTrue(home.isEntranceButtonDisplayed() && home.isEntranceButtonClickable());

        LoginPom login = home.enter();

        assertTrue(login.isDisplayed());

        login.setEmail(REGISTERED_EMAIL);
        login.setPassword(REGISTERED_PASSWORD);

        home = login.enter();
        Selenide.Wait().withTimeout(Duration.ofSeconds(5))
                .until(webDriver -> finalHome.isDisplayed());

        assertTrue(home.isOrderButtonDisplayed() && home.isOrderButtonClickable());
    }

}
