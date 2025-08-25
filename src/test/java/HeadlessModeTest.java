import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HeadlessModeTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HeadlessModeTest.class);

    @BeforeEach
    void setupHeadless(TestInfo testInfo) {
        commonSetup(testInfo);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        initializeDriver(options);
    }

    @Test
    @Order(1)
    @DisplayName("Тест 1: Ввод текста в headless режиме")
    void testInputHeadless() {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.id("textInput")));

        String text = "ОТУС";
        input.sendKeys(text);
        logger.info("Ввели текст: {}", text);

        // Ждем, пока атрибут value обновится
        wait.until(ExpectedConditions.attributeToBe(input, "value", text));

        String actualValue = input.getAttribute("value");
        logger.info("Фактическое значение в поле: '{}'", actualValue);
        logger.info("Ожидаемое значение: '{}'", text);

        assertEquals(text, actualValue, "Текст в поле совпадает");
    }
}