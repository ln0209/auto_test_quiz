package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.zh_cn.假设;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.并且;
import io.cucumber.java.zh_cn.那么;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import utils.BrowserFactory;
import utils.ConfigReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class EmployeeClaimsSteps {

    private WebDriver driver;
    private WebDriverWait wait;
    private Scenario scenario;
    private String currentClaimId;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;

        // 使用浏览器工厂创建驱动
        driver = BrowserFactory.createDriver();

        // 配置等待时间
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("implicit.wait"))));

        wait = new WebDriverWait(driver,
                Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("explicit.wait"))));

        // 设置页面加载超时
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("page.load.timeout"))));

        takeScreenshot("浏览器启动完成");
    }

    @假设("用户打开测试网站 {string}")
    public void userOpensTestWebsite(String url) {
        driver.get(url);
        takeScreenshot("打开测试网站 - " + ConfigReader.getBrowserType());

        // 如果是 IE Mode，可能需要额外的等待和处理
        if ("edge-ie-mode".equalsIgnoreCase(ConfigReader.getBrowserType())) {
            handleIEModeCompatibility();
        }
    }

    private void handleIEModeCompatibility() {
        try {
            // IE Mode 可能需要额外的兼容性处理
            Thread.sleep(2000); // 等待页面完全加载

            // 处理可能的安全警告
            try {
                driver.switchTo().alert().accept();
            } catch (Exception e) {
                // 没有alert，继续执行
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 其他步骤实现保持不变...

    @After
    public void tearDown() {
        // 使用浏览器工厂的清理方法
        BrowserFactory.cleanupDriver(driver);
    }

    private void takeScreenshot(String screenshotName) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String browserInfo = "[" + ConfigReader.getBrowserType().toUpperCase() + "] ";
            scenario.attach(screenshot, "image/png", browserInfo + screenshotName);

            // 保存到文件系统
            String timestamp = String.valueOf(System.currentTimeMillis());
            String browserType = ConfigReader.getBrowserType();
            Path path = Paths.get("screenshots", browserType, scenario.getName(), timestamp + "_" + screenshotName + ".png");
            Files.createDirectories(path.getParent());
            Files.write(path, screenshot);
        } catch (IOException e) {
            System.err.println("截图保存失败: " + e.getMessage());
        }
    }
}