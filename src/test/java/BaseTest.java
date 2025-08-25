import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected static TestConfig config;
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        config = ConfigFactory.create(TestConfig.class);
        logger.info("Base URL loaded from config: {}", config.baseUrl());
    }


    protected void commonSetup(TestInfo testInfo) {
        logger.info("Старт теста: {}", testInfo.getDisplayName());
    }

    protected void initializeDriver(ChromeOptions options) {
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(config.baseUrl());
        logger.info("Открыта страница {}", config.baseUrl());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Браузер закрыт");
        }
    }
}