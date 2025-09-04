package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserFactory {

    public static WebDriver createDriver() {
        String browserType = ConfigReader.getProperty("browser", "chrome");
        System.out.println("正在创建浏览器驱动，类型: " + browserType);

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
        // 自动下载和管理 ChromeDriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        return new ChromeDriver(options);
    }

    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--inprivate");

        return new EdgeDriver(options);
    }

    private static WebDriver createEdgeIEModeDriver() {
        // 注意：IE Mode 可能仍然需要手动配置
        // 这里先使用普通 Edge 模式
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");

        // IE Mode 配置（可能需要额外设置）
        try {
            options.addArguments("--ie-mode-force");
            String siteList = ConfigReader.getIESiteList();
            if (siteList != null && !siteList.isEmpty()) {
                options.addArguments("--ie-mode-site-list=" + siteList);
            }

            InternetExplorerOptions ieOptions = new InternetExplorerOptions();
            ieOptions.ignoreZoomSettings();
            ieOptions.introduceFlakinessByIgnoringSecurityDomains();
            options.setCapability(InternetExplorerOptions.IE_OPTIONS, ieOptions);
        } catch (Exception e) {
            System.out.println("IE Mode 配置失败，使用普通模式: " + e.getMessage());
        }

        return new EdgeDriver(options);
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