package org.example.cardshop.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CardShopSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "/path/to/your/geckodriver"); // PRECISA COLAR O SEU CAMINHO AQUI
        driver = new FirefoxDriver();
    }

    @Test
    public void testAddCard() {
        driver.get("http://localhost:" + port + "/");

        driver.findElement(By.linkText("Add Card")).click();

        WebElement nameField = driver.findElement(By.id("name"));
        WebElement descriptionField = driver.findElement(By.id("description"));
        WebElement priceField = driver.findElement(By.id("price"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));

        nameField.sendKeys("Test Card");
        descriptionField.sendKeys("Test Description");
        priceField.sendKeys("10.0");
        submitButton.click();

        driver.get("http://localhost:" + port + "/");
        WebElement cardName = driver.findElement(By.xpath("//td[text()='Test Card']"));
        assertEquals("Test Card", cardName.getText());
    }

    @Test
    public void testEditCard() {
        driver.get("http://localhost:" + port + "/add");
        driver.findElement(By.id("name")).sendKeys("Card to Edit");
        driver.findElement(By.id("description")).sendKeys("Description");
        driver.findElement(By.id("price")).sendKeys("15.0");
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.xpath("//td[text()='Card to Edit']/following-sibling::td/a[text()='Edit']")).click();

        WebElement nameField = driver.findElement(By.id("name"));
        nameField.clear();
        nameField.sendKeys("Edited Card");
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        driver.get("http://localhost:" + port + "/");
        WebElement cardName = driver.findElement(By.xpath("//td[text()='Edited Card']"));
        assertEquals("Edited Card", cardName.getText());
    }

    @Test
    public void testDeleteCard() {
        driver.get("http://localhost:" + port + "/add");
        driver.findElement(By.id("name")).sendKeys("Card to Delete");
        driver.findElement(By.id("description")).sendKeys("Description");
        driver.findElement(By.id("price")).sendKeys("20.0");
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        driver.get("http://localhost:" + port + "/");
        WebElement deleteLink = driver.findElement(By.xpath("//td[text()='Card to Delete']/following-sibling::td/a[text()='Delete']"));
        deleteLink.click();

        driver.get("http://localhost:" + port + "/");
        assertEquals(0, driver.findElements(By.xpath("//td[text()='Card to Delete']")).size());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
