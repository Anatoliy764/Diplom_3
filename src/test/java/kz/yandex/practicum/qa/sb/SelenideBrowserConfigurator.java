package kz.yandex.practicum.qa.sb;

import com.codeborne.selenide.Configuration;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.safari.SafariOptions;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class SelenideBrowserConfigurator {

    private static final String ENV_BROWSER = "BROWSER";

    private static final String ENV_BROWSER_OPTIONS = "BROWSER_OPTIONS";

    public static void configure() {
        String browser = Optional.ofNullable(System.getenv(ENV_BROWSER)).orElse(BrowserType.CHROME);
        String[] options = Optional.ofNullable(System.getenv(ENV_BROWSER_OPTIONS)).orElse("--incognito").split("\\s+");
        configure(browser, createOptions(browser, options));
    }

    public static void configure(String browser,
                                 MutableCapabilities capabilities) {
        configure(browser, capabilities, null);
    }

    public static void configure(String browser,
                                 MutableCapabilities capabilities,
                                 String binariesPath) {

        if(browser != null && !browser.isEmpty()) {
            if(!Objects.equals(Configuration.browser, browser)) {
                if(browser.equalsIgnoreCase("yandex")) {
                    configureYandexBrowser();
                } else {
                    Configuration.browser = browser;
                    if(binariesPath != null && !binariesPath.isBlank()) {
                        Configuration.browserBinary = binariesPath;
                    }
                }
            }
            if(!Objects.equals(Configuration.browserCapabilities, capabilities)) {
                Configuration.browserCapabilities = capabilities;
            }
        } else {
            throw new IllegalArgumentException("Please provide a browser");
        }
    }

    private static void configureYandexBrowser() {
        System.out.println("Selenide is configured for Yandex browser");

        String yandexWebDriverPath = System.getenv("YANDEX_WEB_DRIVER_PATH");

        if (yandexWebDriverPath != null && !yandexWebDriverPath.isBlank()) {
            // Use the provided environment variable path
            System.setProperty("webdriver.chrome.driver", yandexWebDriverPath);
        } else {
            // Detect the OS and build the default path for the Yandex WebDriver
            String osSpecificPath = getWebDriverPath();
            if (osSpecificPath != null) {
                System.setProperty("webdriver.chrome.driver", osSpecificPath);
                System.out.println("Using default Yandex WebDriver path: " + osSpecificPath);
            } else {
                throw new RuntimeException("Required environment variable YANDEX_WEB_DRIVER_PATH is not set, and no default WebDriver was found for the current OS.");
            }
        }
    }

    private static String getWebDriverPath() {
        // Detect the OS
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        // Define relative path based on OS
        String relativePath;
        if (osName.contains("win")) {
            // Windows
            relativePath = "src/test/resources/webDriverBinaries/windows/yandex/25.2.0.2123/yandexdriver.exe";
        } else if (osName.contains("mac")) {
            // macOS
            relativePath = "src/test/resources/webDriverBinaries/macos/yandex/25.2.6.694/yandexdriver";
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            // Unix/Linux
            relativePath = "src/test/resources/webDriverBinaries/unix/yandex/25.2.6.703/yandexdriver";
        } else {
            // Unknown OS
            throw new RuntimeException("Unsupported operating system: " + osName);
        }

        // Convert the relative path to an absolute path
        Path defaultDriverPath = Paths.get(relativePath).toAbsolutePath();

        // Check if the file exists before returning
        if (defaultDriverPath.toFile().exists()) {
            return defaultDriverPath.toString();
        } else {
            System.err.println("Default WebDriver binary not found at: " + defaultDriverPath);
            return null;
        }
    }



    private static MutableCapabilities createOptions(String browser, String[] options) {
        if (browser == null) {
            throw new IllegalArgumentException("browser may not be null");
        }
        switch (browser) {
            case BrowserType.CHROME:
            case "yandex": {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(options);
                return chromeOptions;
            }
            case BrowserType.FIREFOX: {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments(options);
                return firefoxOptions;
            }
            case BrowserType.SAFARI: {
                SafariOptions safariOptions = new SafariOptions();
                safariOptions.setUseTechnologyPreview(true);
                return safariOptions;
            }
            default:
                return null;
        }
    }
}
