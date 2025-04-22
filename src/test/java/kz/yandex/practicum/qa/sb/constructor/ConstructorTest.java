package kz.yandex.practicum.qa.sb.constructor;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import kz.yandex.practicum.qa.sb.SelenideBrowserConfigurator;
import kz.yandex.practicum.qa.sb.pom.ConstructorPom;
import kz.yandex.practicum.qa.sb.pom.HomePom;
import kz.yandex.practicum.qa.sb.pom.auth.LoginPom;
import kz.yandex.practicum.qa.sb.rest.common.ApiException;
import kz.yandex.practicum.qa.sb.rest.user.User;
import kz.yandex.practicum.qa.sb.rest.user.UserRestClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static kz.yandex.practicum.qa.sb.FakerInstance.FAKER;

/**
 * Задание 3: веб-приложение
 * Раздел «Конструктор»
 * Проверь, что работают переходы к разделам:
 * «Булки»,
 * «Соусы»,
 * «Начинки».
 */
@Slf4j
@RunWith(Parameterized.class)
public class ConstructorTest {

    private static final AtomicReference<User> REGISTERED_USER = new AtomicReference<>();

    private static JavascriptExecutor jsExecutor;

    private static ConstructorPom constructorPom;

    private static SelenideElement scrollableDiv;

    private final ConstructorPom.Tab[] TABS;

    public ConstructorTest(String browser, MutableCapabilities browserOptions, ConstructorPom.Tab[] tabs) {
        SelenideBrowserConfigurator.configure(browser, browserOptions);
        TABS = tabs;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        ConstructorPom.Tab[] tabs = {ConstructorPom.Tab.FILLINGS, ConstructorPom.Tab.SAUCES, ConstructorPom.Tab.BUNS};

        return new Object[][]{
                {BrowserType.CHROME, new ChromeOptions().addArguments("--incognito"), tabs},
                {"yandex", new ChromeOptions().addArguments("--incognito"), tabs}
        };
    }

    @Before
    public void beforeParam() {

        System.out.println("before param executed");

        LoginPom loginPom = Selenide.open("https://stellarburgers.nomoreparties.site/login", LoginPom.class);

        loginPom.setEmail(REGISTERED_USER.get().getEmail());
        loginPom.setPassword(REGISTERED_USER.get().getPassword());

        HomePom homePom = loginPom.clickEntranceButton();

        constructorPom = homePom.getHeader().clickConstructorAnchor();

        scrollableDiv = Selenide.$(".BurgerIngredients_ingredients__menuContainer__Xu3Mo");

        jsExecutor = (JavascriptExecutor) Selenide.webdriver().object();
    }

    @After
    public void after() {
        Selenide.closeWebDriver();
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

    // При клике на вкладку конструктора ползунок меняет свою позицию и отображает соответсвующий контент.
    // Проверять отображаемый контент нет смысла, т.к. при загрузке конструктора страница содержит весь контент,
    // а не подгружает его динамически в соответствии с выбранной вкладкой.
    // Иначе говоря нет смысла проверять отображается ли к примеру начинка Мясо бессмертных моллюсков Protostomia,
    // т.к. даже если текущая вкладка это "Булки", начинка находится на странице.
    // Поэтому проверяем что ползунок двигается при выборе вкладки.
    @Test
    public void testClickTabs() {
        for (ConstructorPom.Tab tab : TABS) {
            Number initialScrollPos = (Number) jsExecutor.executeScript("return arguments[0].scrollTop;", scrollableDiv);

            constructorPom.clickTab(tab);

            ConstructorPom.Tab currentTab = constructorPom.getCurrentTab();

            Assert.assertTrue(currentTab != null && Objects.equals(tab, currentTab));

            Number currentScrollPos = (Number) jsExecutor.executeScript("return arguments[0].scrollTop;", scrollableDiv);

            Assert.assertNotEquals(initialScrollPos.doubleValue(), currentScrollPos.doubleValue());
        }
    }
}
