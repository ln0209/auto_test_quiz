package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;

public class BrowserFactory {

    public static WebDriver createDriver() {
        String browserType = ConfigReader.getProperty("browser", "chrome");

        switch (browserType.toLowerCase()) {
            case "chrome":
                return createChromeDriver();
            case "edge":
                return createEdgeDriver();
            case "edge-ie-mode":
                return createEdgeIEModeDriver();
            default:
                throw new IllegalArgumentException("不支持的浏览器类型: " + browserType);
        }
    }

    private static WebDriver createChromeDriver() {
        System.setProperty("webdriver.chrome.driver", ConfigReader.getChromeDriverPath());

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");

        return new ChromeDriver(options);
    }

    private static WebDriver createEdgeDriver() {
        System.setProperty("webdriver.edge.driver", ConfigReader.getEdgeDriverPath());

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--inprivate");

        return new EdgeDriver(options);
    }

    private static WebDriver createEdgeIEModeDriver() {
        // 首先设置 IE Driver（IE Mode 需要）
        System.setProperty("webdriver.ie.driver", ConfigReader.getIEDriverPath());

        // 配置 Edge 使用 IE Mode
        EdgeOptions options = new EdgeOptions();
        options.setCapability("ms:edgeOptions", getIEModeCapabilities());
        options.setCapability(EdgeOptions.CAPABILITY, getIEModeCapabilities());

        // 设置 IE 选项（兼容性设置）
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        ieOptions.ignoreZoomSettings();
        ieOptions.introduceFlakinessByIgnoringSecurityDomains();
        ieOptions.enablePersistentHovering();

        options.setCapability(InternetExplorerOptions.IE_OPTIONS, ieOptions);

        System.setProperty("webdriver.edge.driver", ConfigReader.getEdgeDriverPath());
        return new EdgeDriver(options);
    }

    private static String getIEModeCapabilities() {
        // 创建 IE Mode 配置
        return String.format("{\"ms:edgeChromium\": true, \"ms:edgeOptions\": " +
                        "{\"args\": [\"--ie-mode-force\"], " +
                        "\"prefs\": {\"browser.edge.ie-mode.enabled\": true, " +
                        "\"browser.edge.ie-mode.site-list\": \"%s\"}}}",
                ConfigReader.getProperty("ie.site.list"));
    }

    public static void cleanupDriver(WebDriver driver) {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("关闭浏览器时出错: " + e.getMessage());
            }
        }
    }
}