package kz.yandex.practicum.qa.sb.auth;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.AccountPom;
import kz.yandex.practicum.qa.sb.pom.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.rest.common.ApiException;
import kz.yandex.practicum.qa.sb.rest.user.User;
import kz.yandex.practicum.qa.sb.rest.user.UserRestClient;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;

import java.util.concurrent.atomic.AtomicReference;

import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;

/**
 * Задание 3: веб-приложение
 * Выход из аккаунта
 * Проверь выход по кнопке «Выйти» в личном кабинете.
 * */
@RunWith(Parameterized.class)
public class LogoutTest {

    private static final AtomicReference<User> REGISTERED_USER = new AtomicReference<>();

    private static AccountPom accountPom;

    public LogoutTest(String browser, MutableCapabilities browserOptions) {
        SelenideBrowserConfigurator.configure(browser, browserOptions);
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                {BrowserType.CHROME, new ChromeOptions().addArguments("--incognito")},
                {"yandex", new ChromeOptions().addArguments("--incognito")}
        };
    }

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

    @Before
    public void beforeTest() {
        LoginPom loginPom = Selenide.open("https://stellarburgers.nomoreparties.site/login", LoginPom.class);

        loginPom.setEmail(REGISTERED_USER.get().getEmail());
        loginPom.setPassword(REGISTERED_USER.get().getPassword());

        HomePom homePom = loginPom.clickEntranceButton();

        accountPom = homePom.getHeader().clickPersonalAccountAnchor(AccountPom.class);
    }

    @After
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    @DisplayName("Выход из аккаунта")
    @Description("выход по кнопке «Выйти» в личном кабинете.")
    public void testLogout() {
        LoginPom loginPom = accountPom.clickLogoutAnchor();

        Assert.assertTrue(loginPom.isDisplayed());
    }
}
