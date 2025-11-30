package org.example.cardshop.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.cardshop.selenium.pages.AddCardPage;
import org.example.cardshop.selenium.pages.EditCardPage;
import org.example.cardshop.selenium.pages.HomePage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CardShopSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private HomePage homePage;
    private AddCardPage addCardPage;
    private EditCardPage editCardPage;
    private String baseUrl;

    @BeforeAll
    public static void setUpClass() {
        try {
            WebDriverManager.firefoxdriver().setup();
            System.out.println("✅ FirefoxDriver configured successfully");
        } catch (Exception e) {
            System.err.println("❌ Error setting up WebDriverManager: " + e.getMessage());
            throw e;
        }
    }

    @BeforeEach
    public void setUp() {
        try {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");

            // Timeout configurations
            options.setPageLoadTimeout(java.time.Duration.ofSeconds(30));

            System.out.println("🔧 Creating Firefox driver with headless mode...");
            driver = new FirefoxDriver(options);
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
            System.out.println("✅ Firefox driver created successfully");

            baseUrl = "http://localhost:" + port;
            homePage = new HomePage(driver);
            addCardPage = new AddCardPage(driver);
            editCardPage = new EditCardPage(driver);
        } catch (Exception e) {
            System.err.println("❌ Error creating Firefox driver: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testAddCard() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        addCardPage.addCard("Test Card", "Test Description", "10.0");

        homePage.navigateTo(baseUrl + "/");
        WebElement cardName = homePage.findCardInList("Test Card");
        assertEquals("Test Card", cardName.getText());
    }

    @Test
    public void testEditCard() {
        addCardPage.navigateTo(baseUrl + "/add");
        addCardPage.addCard("Card to Edit", "Description", "15.0");

        homePage.navigateTo(baseUrl + "/");
        homePage.getEditLinkForCard("Card to Edit").click();

        editCardPage.editCardName("Edited Card");

        homePage.navigateTo(baseUrl + "/");
        WebElement cardName = homePage.findCardInList("Edited Card");
        assertEquals("Edited Card", cardName.getText());
    }

    @Test
    public void testDeleteCard() {
        addCardPage.navigateTo(baseUrl + "/add");
        addCardPage.addCard("Card to Delete", "Description", "20.0");

        homePage.navigateTo(baseUrl + "/");
        homePage.getDeleteLinkForCard("Card to Delete").click();

        homePage.navigateTo(baseUrl + "/");
        assertEquals(0, homePage.getCardCount("Card to Delete"));
    }


    @Test
    public void testAddCard_WithSpecialCharacters() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        addCardPage.addCard("Card @#$%", "Description with !@#$", "25.99");

        homePage.navigateTo(baseUrl + "/");
        WebElement cardName = homePage.findCardInList("Card @#$%");
        assertEquals("Card @#$%", cardName.getText());
    }

    @Test
    public void testAddCard_WithLongName() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        String longName = "This is a very long card name with many characters to test the system behavior";
        addCardPage.addCard(longName, "Test Description", "15.50");

        homePage.navigateTo(baseUrl + "/");
        WebElement cardName = homePage.findCardInList(longName);
        assertEquals(longName, cardName.getText());
    }

    @Test
    public void testAddCard_WithDecimalPrice() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        addCardPage.addCard("Decimal Price Card", "Testing decimal prices", "99.99");

        homePage.navigateTo(baseUrl + "/");
        WebElement cardName = homePage.findCardInList("Decimal Price Card");
        assertEquals("Decimal Price Card", cardName.getText());
    }

    @Test
    public void testAddCard_WithEmptyName_ShouldShowError() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        addCardPage.addCard("", "Test Description", "10.0");

        assertFalse(addCardPage.isSubmitButtonEnabled(), "Submit button should be disabled when name is empty");
    }

    @Test
    public void testAddCard_WithEmptyDescription_ShouldShowError() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        addCardPage.addCard("Test Card", "", "10.0");

        assertFalse(addCardPage.isSubmitButtonEnabled(), "Submit button should be disabled when description is empty");
    }

    @Test
    public void testAddCard_WithNegativePrice_ShouldShowError() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        addCardPage.addCard("Test Card", "Test Description", "-10.0");

        assertFalse(addCardPage.isSubmitButtonEnabled(), "Submit button should be disabled when price is negative");
    }

    @Test
    public void testAddCard_WithZeroPrice_ShouldShowError() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        addCardPage.addCard("Test Card", "Test Description", "0");

        assertFalse(addCardPage.isSubmitButtonEnabled(), "Submit button should be disabled when price is zero");
    }

    @Test
    public void testAddCard_WithBlankName_ShouldShowError() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        addCardPage.addCard("   ", "Test Description", "10.0");

        assertFalse(addCardPage.isSubmitButtonEnabled(), "Submit button should be disabled when name is blank");
    }

    @Test
    public void testAddCard_WithAllFieldsEmpty_ShouldShowMultipleErrors() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        addCardPage.addCard("", "", "");

        assertFalse(addCardPage.isSubmitButtonEnabled(), "Submit button should be disabled when all fields are empty");
    }

    @Test
    public void testEditCard_WithEmptyName_ShouldShowError() {
        addCardPage.navigateTo(baseUrl + "/add");
        addCardPage.addCard("Card to Edit", "Description", "15.0");

        homePage.navigateTo(baseUrl + "/");
        homePage.getEditLinkForCard("Card to Edit").click();

        editCardPage.editCardName("");

        assertFalse(editCardPage.isSubmitButtonEnabled(), "Submit button should be disabled when name is empty");
    }

    @Test
    public void testAddCard_ShouldStayOnPageWhenValidationFails() {
        homePage.navigateTo(baseUrl + "/");
        homePage.clickAddCard();

        addCardPage.addCard("", "Test Description", "10.0");

        assertEquals(true, addCardPage.isOnAddPage());
    }


    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
