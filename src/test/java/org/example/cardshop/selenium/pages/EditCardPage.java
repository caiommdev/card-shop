package org.example.cardshop.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class EditCardPage {

    private WebDriver driver;

    private By nameField = By.id("name");
    private By submitButton = By.cssSelector("input[type='submit']");

    public EditCardPage(WebDriver driver) {
        this.driver = driver;
    }

    public void editCardName(String newName) {
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(nameField));

        WebElement nameElement = driver.findElement(nameField);

        js.executeScript("arguments[0].value = '';", nameElement);
        if (newName != null && !newName.isEmpty()) {
            nameElement.sendKeys(newName);
        }

        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", nameElement);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        driver.findElement(submitButton).click();

        wait.until(driver ->
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete")
        );
    }

    public String getNameError() {
        try {
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
            System.out.println("Error getting name error message in EditCardPage: " + e.getMessage());
            return "";
        }
    }

    public String getDescriptionError() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement errorElement = wait.until(driver -> {
                List<WebElement> errors = driver.findElements(
                    By.xpath("//input[@id='description']/following-sibling::div[contains(@class, 'invalid-feedback')]")
                );
                for (WebElement error : errors) {
                    if (error.isDisplayed() && !error.getText().trim().isEmpty()) {
                        return error;
                    }
                }
                return null;
            });
            return errorElement != null ? errorElement.getText().trim() : "";
        } catch (Exception e) {
            System.out.println("Error getting description error message in EditCardPage: " + e.getMessage());
            return "";
        }
    }

    public String getPriceError() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement errorElement = wait.until(driver -> {
                List<WebElement> errors = driver.findElements(
                    By.xpath("//input[@id='price']/following-sibling::div[contains(@class, 'invalid-feedback')]")
                );
                for (WebElement error : errors) {
                    if (error.isDisplayed() && !error.getText().trim().isEmpty()) {
                        return error;
                    }
                }
                return null;
            });
            return errorElement != null ? errorElement.getText().trim() : "";
        } catch (Exception e) {
            System.out.println("Error getting price error message in EditCardPage: " + e.getMessage());
            return "";
        }
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

