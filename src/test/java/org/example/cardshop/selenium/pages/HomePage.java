package org.example.cardshop.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {

    private final WebDriver driver;

    private final By addCardLink = By.linkText("Add Card");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void clickAddCard() {
        driver.findElement(addCardLink).click();
    }

    public WebElement findCardInList(String cardName) {
        return driver.findElement(By.xpath("//td[text()='" + cardName + "']"));
    }

    public WebElement getEditLinkForCard(String cardName) {
        return driver.findElement(By.xpath("//td[text()='" + cardName + "']/following-sibling::td/a[text()='Edit']"));
    }

    public WebElement getDeleteLinkForCard(String cardName) {
        return driver.findElement(By.xpath("//td[text()='" + cardName + "']/following-sibling::td/a[text()='Delete']"));
    }

    public int getCardCount(String cardName) {
        return driver.findElements(By.xpath("//td[text()='" + cardName + "']")).size();
    }
}

