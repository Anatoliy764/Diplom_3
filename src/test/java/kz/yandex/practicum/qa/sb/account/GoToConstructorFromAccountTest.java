package kz.yandex.practicum.qa.sb.account;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.AccountPom;
import kz.yandex.practicum.qa.sb.pom.ConstructorPom;
import kz.yandex.practicum.qa.sb.pom.HeaderPom;
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
 * Переход из личного кабинета в конструктор
 * Проверь переход по клику на «Конструктор» и на логотип Stellar Burgers.
 * */
@RunWith(Parameterized.class)
public class GoToConstructorFromAccountTest {

    private static final AtomicReference<User> REGISTERED_USER = new AtomicReference<>();

    private static AccountPom account;

    public GoToConstructorFromAccountTest(String browser, MutableCapabilities browserOptions) {
        SelenideBrowserConfigurator.configure(browser, browserOptions);
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                {BrowserType.CHROME, new ChromeOptions().addArguments("--incognito")},
                {"yandex", new ChromeOptions().addArguments("--incognito")}
        };
    }

    // создаем пользователя для теста аутентификации
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

        HomePom home = loginPom.clickEntranceButton();

        HeaderPom header = home.getHeader();

        account = header.clickPersonalAccountAnchor(AccountPom.class);
    }

    @After
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    @DisplayName("Переход из личного кабинета в конструктор")
    @Description("переход по клику на «Конструктор» и на логотип Stellar Burgers.")
    public void goToConstructorTest() {
        HeaderPom header = account.getHeader();

        ConstructorPom constructorPom = header.clickConstructorAnchor();

        Assert.assertEquals("Соберите бургер", constructorPom.getHeading());
    }
}
