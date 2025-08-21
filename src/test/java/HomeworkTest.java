import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HomeworkTest {
    private static final String URL = "https://otus.home.kartushin.su/training.html";
    private static final Logger logger = LogManager.getLogger(HomeworkTest.class);
    private WebDriver driver;

    @BeforeEach
    void setup(TestInfo testInfo) {
        WebDriverManager.chromedriver().setup();
        logger.info("Старт теста: {}", testInfo.getDisplayName());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Браузер закрыт");
        }
    }

    @Test
    @Order(1)
    @DisplayName("Тест 1: Ввод текста в headless режиме")
    void testInputHeadless() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);

        try {
            driver.get(URL);
            logger.info("Открыта страница {}", URL);

            // Добавляем ожидание загрузки страницы
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Ждем пока элемент станет кликабельным
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.id("textInput")));

            String text = "ОТУС";
            input.sendKeys(text);
            logger.info("Ввели текст: {}", text);

            // Даем время на обновление атрибута
            Thread.sleep(500);

            // Получаем значение атрибута и логируем его
            String actualValue = input.getAttribute("value");
            logger.info("Фактическое значение в поле: '{}'", actualValue);
            logger.info("Ожидаемое значение: '{}'", text);

            assertEquals(text, actualValue, "Текст в поле совпадает");

        } catch (Exception e) {
            logger.error("Ошибка в тесте: ", e);
            throw new RuntimeException(e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    @Order(2)
    @DisplayName("Тест 2: Модальное окно в режиме киоска")
    void testModalKiosk() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--kiosk");
        driver = new ChromeDriver(options);

        try {
            driver.get(URL);
            logger.info("Открыта страница {}", URL);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Ждем пока кнопка станет кликабельной
            WebElement openModalBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("openModalBtn")));
            openModalBtn.click();
            logger.info("Кликнули 'Открыть модальное окно'");

            // Ждем пока модальное окно станет видимым
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myModal")));
            assertTrue(modal.isDisplayed(), "Модальное окно не открылось");
            logger.info("Модальное окно успешно открылось");

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("Тест 3: Отправка формы в полноэкранном режиме")
    void testFormFullscreen() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);

        driver.get(URL);
        logger.info("Открыта страница {}", URL);

        // Переводим в полноэкранный режим ПОСЛЕ открытия страницы
        driver.manage().window().fullscreen();
        logger.info("Переведено в полноэкранный режим");

        driver.findElement(By.id("name")).sendKeys("Иван");
        driver.findElement(By.id("email")).sendKeys("ivan@test.ru");
        driver.findElement(By.xpath("//button[@type='submit' and text()='Отправить']")).click();
        logger.info("Форма отправлена");

        // Добавляем ожидание появления сообщения
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement message = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messageBox")));

        String actual = message.getText();
        logger.info("Сообщение: {}", actual);

        assertTrue(actual.contains("Форма отправлена с именем: Иван"),
                "Имя в сообщении некорректно");
        assertTrue(actual.contains("email: ivan@test.ru"),
                "Email в сообщении некорректен");
    }
}
