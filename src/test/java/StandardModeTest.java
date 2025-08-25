import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StandardModeTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(StandardModeTest.class);

    @BeforeEach
    void setupStandard(TestInfo testInfo) {
        commonSetup(testInfo);
        ChromeOptions options = new ChromeOptions();
        initializeDriver(options);
    }

    @Test
    @Order(1)
    @DisplayName("Тест 1: Модальное окно в стандартном режиме")
    void testModalStandard() {
        WebElement openModalBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("openModalBtn")));
        openModalBtn.click();
        logger.info("Кликнули 'Открыть модальное окно'");

        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myModal")));
        assertTrue(modal.isDisplayed(), "Модальное окно не открылось");
        logger.info("Модальное окно успешно открылось");
    }

    @Test
    @Order(2)
    @DisplayName("Тест 2: Отправка формы в стандартном режиме")
    void testFormStandard() {
        driver.findElement(By.id("name")).sendKeys("Иван");
        driver.findElement(By.id("email")).sendKeys("ivan@test.ru");
        driver.findElement(By.xpath("//button[@type='submit' and text()='Отправить']")).click();
        logger.info("Форма отправлена");

        WebElement message = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messageBox")));

        String actual = message.getText();
        logger.info("Сообщение: {}", actual);

        assertTrue(actual.contains("Форма отправлена с именем: Иван"),
                "Имя в сообщении некорректно");
        assertTrue(actual.contains("email: ivan@test.ru"),
                "Email в сообщении некорректен");
    }
}