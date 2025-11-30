package org.example.cardshop.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AddCardPage {

    private WebDriver driver;

    private By nameField = By.id("name");
    private By descriptionField = By.id("description");
    private By priceField = By.id("price");
    private By submitButton = By.cssSelector("input[type='submit']");

    public AddCardPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void addCard(String name, String description, String price) {
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(nameField));

        WebElement nameElement = driver.findElement(nameField);
        WebElement descriptionElement = driver.findElement(descriptionField);
        WebElement priceElement = driver.findElement(priceField);

        js.executeScript("arguments[0].value = '';", nameElement);
        if (name != null && !name.isEmpty()) {
            nameElement.sendKeys(name);
        }

        js.executeScript("arguments[0].value = '';", descriptionElement);
        if (description != null && !description.isEmpty()) {
            descriptionElement.sendKeys(description);
        }

        js.executeScript("arguments[0].value = '';", priceElement);
        if (price != null && !price.isEmpty()) {
            priceElement.sendKeys(price);
        }

        driver.findElement(submitButton).click();

        wait.until(driver ->
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete")
        );
    }

    public String getNameError() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));

            Thread.sleep(500);

            List<WebElement> errors = driver.findElements(
                By.xpath("//input[@id='name']/following-sibling::div[contains(@class, 'invalid-feedback')]")
            );

            for (WebElement error : errors) {
                String text = error.getText().trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
            return "";
        } catch (Exception e) {
            System.out.println("Error getting name error message: " + e.getMessage());
            return "";
        }
    }

    public String getDescriptionError() {
        try {
            Thread.sleep(500);

            List<WebElement> errors = driver.findElements(
                By.xpath("//input[@id='description']/following-sibling::div[contains(@class, 'invalid-feedback')]")
            );

            for (WebElement error : errors) {
                String text = error.getText().trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
            return "";
        } catch (Exception e) {
            System.out.println("Error getting description error message: " + e.getMessage());
            return "";
        }
    }

    public String getPriceError() {
        try {
            Thread.sleep(500);

            List<WebElement> errors = driver.findElements(
                By.xpath("//input[@id='price']/following-sibling::div[contains(@class, 'invalid-feedback')]")
            );

            for (WebElement error : errors) {
                String text = error.getText().trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
            return "";
        } catch (Exception e) {
            System.out.println("Error getting price error message: " + e.getMessage());
            return "";
        }
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public boolean isOnAddPage() {
        return driver.getCurrentUrl().contains("/add");
    }

    public boolean hasValidationAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement alert = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("validation-alert")));
            return alert.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getValidationAlertText() {
        try {
            WebElement alert = driver.findElement(By.id("validation-alert"));
            return alert.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isSubmitButtonEnabled() {
        try {
            WebElement submitButton = driver.findElement(By.id("submitBtn"));
            return submitButton.isEnabled() && !submitButton.getAttribute("disabled").equals("true");
        } catch (Exception e) {
            try {
                WebElement submitButton = driver.findElement(By.id("submitBtn"));
                return submitButton.isEnabled();
            } catch (Exception ex) {
                return false;
            }
        }
    }
}
