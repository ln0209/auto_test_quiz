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
                "html:target/cucumber-reports-chrome.html",
                "json:target/cucumber-chrome.json",
                "junit:target/cucumber-chrome.xml"
        },
        tags = "@employee_claims",
        monochrome = true
)
public class ChromeTestRunner {
}