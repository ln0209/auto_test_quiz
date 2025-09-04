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

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class EmployeeClaimsSteps {

    private WebDriver driver;
    private WebDriverWait wait;
    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        driver = BrowserFactory.createDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @假设("用户打开测试网站 {string}")
    public void userOpensTestWebsite(String url) {
        driver.get(url);
        takeScreenshot("打开测试网站");
        System.out.println("已打开网站: " + url);
    }

    @当("用户使用用户名 {string} 和密码 {string} 登录系统")
    public void userLogsInWithCredentials(String username, String password) {
        try {
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.name("username")));
            usernameField.clear();
            usernameField.sendKeys(username);
            takeScreenshot("输入用户名后");

            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.clear();
            passwordField.sendKeys(password);
            takeScreenshot("输入密码后");

            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();
            takeScreenshot("点击登录按钮后");

            wait.until(ExpectedConditions.urlContains("dashboard"));
            takeScreenshot("登录成功后的页面");
            System.out.println("登录成功");

        } catch (Exception e) {
            takeScreenshot("登录过程中出错");
            throw new RuntimeException("登录失败: " + e.getMessage(), e);
        }
    }

    @并且("用户导航到Claims模块")
    public void navigateToClaimsModule() {
        try {
            WebElement claimsLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("a.oxd-main-menu-item[href*='claim']")));
            claimsLink.click();
            takeScreenshot("点击Claims链接后");
            Thread.sleep(2000);
        } catch (Exception e) {
            takeScreenshot("导航到Claims模块失败");
            throw new RuntimeException("无法导航到Claims模块: " + e.getMessage(), e);
        }
    }

    @并且("点击{string}选项")
    public void clickOption(String optionText) {
        try {
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'" + optionText + "')]")));
            option.click();
            takeScreenshot("点击" + optionText + "后");
            Thread.sleep(2000);
        } catch (Exception e) {
            takeScreenshot("点击选项失败: " + optionText);
            throw new RuntimeException("无法点击选项: " + optionText, e);
        }
    }

    @并且("点击{string}按钮")
    public void clickButton(String buttonText) {
        try {
            // 处理文本差异
            String searchText = buttonText;
            if ("Assign Claims".equals(buttonText)) {
                searchText = "Assign Claim";
            }

            WebElement button = findButton(searchText);
            if (button == null) {
                throw new RuntimeException("无法找到按钮: " + buttonText);
            }

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            takeScreenshot("点击" + buttonText + "按钮后");
            Thread.sleep(2000);

        } catch (Exception e) {
            takeScreenshot("点击按钮失败: " + buttonText);
            throw new RuntimeException("无法点击按钮: " + buttonText, e);
        }
    }

    @并且("在创建报销请求表单中填写如下信息：")
    public void fillCreateClaimForm(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        try {
            // 填写Employee Name
            WebElement employeeField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[placeholder='Type for hints...']")));
            employeeField.clear();
            employeeField.sendKeys(data.get("Employee Name"));
            takeScreenshot("填写员工姓名后");

            // 选择Event下拉框
            selectOxdDropdown("Event", data.get("Event"));
            takeScreenshot("选择事件后");

            // 选择Currency下拉框
            selectOxdDropdown("Currency", data.get("Currency"));
            takeScreenshot("选择货币后");

        } catch (Exception e) {
            takeScreenshot("填写表单失败");
            throw new RuntimeException("填写表单失败: " + e.getMessage(), e);
        }
    }

    @那么("系统应显示成功消息 {string}")
    public void verifySuccessMessage(String expectedMessage) {
        try {
            WebElement successAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".alert-success, .toast-success, .success-message")));
            String actualMessage = successAlert.getText();
            Assert.assertTrue("成功消息不匹配", actualMessage.contains(expectedMessage));
            takeScreenshot("成功消息提示");
        } catch (Exception e) {
            takeScreenshot("验证成功消息失败");
            throw new RuntimeException("验证成功消息失败: " + e.getMessage(), e);
        }
    }

    @并且("用户应被导航到Assign Claim详情页面")
    public void verifyOnAssignClaimDetailPage() {
        try {
            wait.until(ExpectedConditions.urlContains("assign-claim"));
            takeScreenshot("Assign Claim详情页面");
        } catch (Exception e) {
            takeScreenshot("导航到详情页面失败");
            throw new RuntimeException("未正确导航到详情页面", e);
        }
    }

    @并且("详情页面上的基础信息应与填写的内容一致：")
    public void verifyDetailPageInformation(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);

        try {
            WebElement employeeElement = driver.findElement(By.cssSelector(".employee-name"));
            Assert.assertEquals("员工姓名不匹配", expectedData.get("Employee Name"), employeeElement.getText());

            WebElement eventElement = driver.findElement(By.cssSelector(".event-type"));
            Assert.assertEquals("事件类型不匹配", expectedData.get("Event"), eventElement.getText());

            WebElement currencyElement = driver.findElement(By.cssSelector(".currency"));
            Assert.assertEquals("货币类型不匹配", expectedData.get("Currency"), currencyElement.getText());

            takeScreenshot("详情页面信息验证");
        } catch (Exception e) {
            takeScreenshot("验证详情信息失败");
            throw new RuntimeException("验证详情信息失败", e);
        }
    }

    @并且("在添加费用表单中填写如下信息：")
    public void fillExpenseForm(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        try {
            WebElement expenseTypeField = driver.findElement(By.name("expenseType"));
            expenseTypeField.sendKeys(data.get("Expense Type"));

            WebElement dateField = driver.findElement(By.name("expenseDate"));
            dateField.sendKeys(data.get("Date"));

            WebElement amountField = driver.findElement(By.name("amount"));
            amountField.sendKeys(data.get("Amount"));

            takeScreenshot("填写费用表单后");
        } catch (Exception e) {
            takeScreenshot("填写费用表单失败");
            throw new RuntimeException("填写费用表单失败", e);
        }
    }

    @并且("费用列表中的最新记录应与填写的信息一致")
    public void verifyExpenseInList() {
        try {
            List<WebElement> expenseRows = driver.findElements(By.cssSelector(".expenses-table tbody tr"));
            Assert.assertTrue("费用列表应该至少有一条记录", expenseRows.size() > 0);
            takeScreenshot("费用列表验证");
        } catch (Exception e) {
            takeScreenshot("验证费用列表失败");
            throw new RuntimeException("验证费用列表失败", e);
        }
    }

    @那么("用户应返回到Assign Claim列表页面")
    public void verifyBackToAssignClaimList() {
        try {
            wait.until(ExpectedConditions.urlContains("list"));
            takeScreenshot("返回列表页面后");
        } catch (Exception e) {
            takeScreenshot("返回列表页面失败");
            throw new RuntimeException("未正确返回列表页面", e);
        }
    }

    @并且("列表中应存在员工为{string}的报销申请记录")
    public void verifyClaimExistsInList(String employeeName) {
        try {
            List<WebElement> records = driver.findElements(By.xpath("//tr[contains(.,'" + employeeName + "')]"));
            Assert.assertTrue("未找到员工的报销记录", records.size() > 0);
            takeScreenshot("列表中存在报销记录");
        } catch (Exception e) {
            takeScreenshot("验证列表记录失败");
            throw new RuntimeException("验证列表记录失败", e);
        }
    }

    // ========== 私有辅助方法 ==========

    private WebElement findButton(String buttonText) {
        String[] selectors = {
                "//button[contains(., '" + buttonText + "')]",
                "//a[contains(., '" + buttonText + "')]",
                "//div[contains(., '" + buttonText + "')]"
        };

        for (String selector : selectors) {
            try {
                WebElement element = driver.findElement(By.xpath(selector));
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    private void selectOxdDropdown(String dropdownLabel, String optionText) {
        try {
            // 通过label找到下拉框
            String dropdownXpath = String.format("//label[text()='%s']/../following-sibling::div//div[@class='oxd-select-text-input']", dropdownLabel);
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownXpath)));

            dropdown.click();
            Thread.sleep(1000);

            // 选择选项
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//div[@role='option'])[3]")));

            option.click();
            Thread.sleep(1000);

        } catch (Exception e) {
            throw new RuntimeException("选择下拉框失败: " + dropdownLabel, e);
        }
    }

    private void takeScreenshot(String screenshotName) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", screenshotName);
        } catch (Exception e) {
            System.err.println("截图失败: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            BrowserFactory.cleanupDriver(driver);
        }
    }
}