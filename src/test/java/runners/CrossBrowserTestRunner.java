package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "steps",
        plugin = {
                "pretty",
                "html:target/cucumber-reports-${browser}.html",
                "json:target/cucumber-${browser}.json",
                "junit:target/cucumber-${browser}.xml"
        },
        tags = "@employee_claims",
        monochrome = true
)
public class CrossBrowserTestRunner {
    // 这个运行器会使用系统属性中的 browser 值
}