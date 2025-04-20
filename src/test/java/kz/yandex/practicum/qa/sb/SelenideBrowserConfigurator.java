package kz.yandex.practicum.qa.sb;

import com.codeborne.selenide.Configuration;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.MutableCapabilities;

@UtilityClass
public class SelenideBrowserConfigurator {

    public static void configure(String browser,
                                 MutableCapabilities capabilities) {
        configure(browser, capabilities, null);
    }

    public static void configure(String browser,
                                 MutableCapabilities capabilities,
                                 String binariesPath) {

        if(browser != null && !browser.isEmpty()) {
            if(browser.equalsIgnoreCase("yandex")) {
                configureYandexBrowser();
            } else {
                Configuration.browser = browser;
                if(binariesPath != null && !binariesPath.isBlank()) {
                    Configuration.browserBinary = binariesPath;
                }
            }
            Configuration.browserCapabilities = capabilities;
        } else {
            throw new IllegalArgumentException("Please provide a browser");
        }
    }

    private static void configureYandexBrowser() {
        String yandexWebDriverPath = System.getenv("YANDEX_WEB_DRIVER_PATH");
        if(yandexWebDriverPath != null && !yandexWebDriverPath.isBlank()) {
            System.setProperty("webdriver.chrome.driver", yandexWebDriverPath);
        } else {
            throw new RuntimeException("Required environment variable YANDEX_WEB_DRIVER_PATH is not set. Please set absolute path to yandex web driver.");
        }
    }
}
