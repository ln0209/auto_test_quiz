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
                "html:target/cucumber-reports-edge-ie-mode.html",
                "json:target/cucumber-edge-ie-mode.json",
                "junit:target/cucumber-edge-ie-mode.xml"
        },
        tags = "@employee_claims",
        monochrome = true
)
public class EdgeIEModeTestRunner {
}