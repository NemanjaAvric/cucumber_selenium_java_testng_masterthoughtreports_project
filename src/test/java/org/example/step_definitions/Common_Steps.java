package org.example.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.utility.Add;
import org.example.utility.URL;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class Common_Steps {

    private WebDriver driver;

    private Scenario scenario;


    @Before(order = 0)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        if (System.getProperty("browser").equalsIgnoreCase("chrome")) {
            this.driver = new ChromeDriver(getChromeOptions());
        } else {
            this.driver = new FirefoxDriver();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Before(order = 1)
    public void browserInfo(Scenario scenario) {
        this.scenario = scenario;
        String browser = System.getProperty("browser");

        String formattedBrowserInfo = "<p style=\"color:blue;\">Browser Information:</p>"
                + "<ul>"
                + "<li><strong>Browser:</strong> " + browser + "</li>"
                + "</ul>";

        if (browser != null) {
            scenario.attach(formattedBrowserInfo, "text/html", "Browser Info - " + scenario.getName());
        } else {
            scenario.attach("Browser information not found", "text/plain", "Browser Info - " + scenario.getName());
        }
    }


    @Given("I am on the Home Page")
    public void i_am_on_the_home_page() {
        driver.get(URL.HOME_PAGE_URL);
        Add.closeAdd(driver);
    }

    @After(order = 1)
    public void takeScraenshotOnFailure(Scenario scenario) {

        if (scenario.isFailed()) {

            TakesScreenshot ts = (TakesScreenshot) driver;

            byte[] src = ts.getScreenshotAs(OutputType.BYTES);
            scenario.attach(src, "image/png", "screenshot");
        }

    }

    @After(order = 0)
    public void tearDown() throws InterruptedException {
        driver.quit();
        Thread.sleep(1000);
    }

    public WebDriver getWebDriver() {
        return driver;
    }

    public ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        //   options.addArguments("start-maximized");
        options.addArguments("test-type");
        options.addArguments("disable-notifications");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("autofill.profile_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        return options;
    }
}
