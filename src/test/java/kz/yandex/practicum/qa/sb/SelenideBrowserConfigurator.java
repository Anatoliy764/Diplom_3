package kz.yandex.practicum.qa.sb;

import com.codeborne.selenide.Configuration;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.MutableCapabilities;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;

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



//    private static void configureYandexBrowser() {
//        System.out.println("Selenide is configured for yandex browser");
//        String yandexWebDriverPath = System.getenv("YANDEX_WEB_DRIVER_PATH");
//
//        if(yandexWebDriverPath != null && !yandexWebDriverPath.isBlank()) {
//            System.setProperty("webdriver.chrome.driver", yandexWebDriverPath);
//        } else {
//            throw new RuntimeException("Required environment variable YANDEX_WEB_DRIVER_PATH is not set. Please set absolute path to yandex web driver.");
//        }
//    }
}
