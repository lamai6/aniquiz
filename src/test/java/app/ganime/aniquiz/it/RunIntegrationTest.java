package app.ganime.aniquiz.it;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
	glue = {"app.ganime.aniquiz.it"},
	features = "src/test/resources/features",
	plugin = {"pretty", "html:target/cucumber/it.html"}
)
public class RunIntegrationTest {
}
