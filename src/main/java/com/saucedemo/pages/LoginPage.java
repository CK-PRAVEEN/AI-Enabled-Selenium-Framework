package com.saucedemo.pages;

import com.saucedemo.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By productsTitle = By.cssSelector(".title");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ---- Private Helpers ----

    private void enterText(By locator, String text) {
        WaitUtils.defaultWait(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(locator))
                .clear();
        WaitUtils.defaultWait(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(locator))
                .sendKeys(text);
    }

    private void clickOnElement(By locator) {
        WaitUtils.defaultWait(driver)
                .until(ExpectedConditions.elementToBeClickable(locator))
                .click();
    }

    private boolean isElementPresent(By locator) {
        try {
            WaitUtils.defaultWait(driver)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ---- Page Actions ----

    public void enterUsername(String username) {
        enterText(usernameInput, username);
    }

    public void enterPassword(String password) {
        enterText(passwordInput, password);
    }

    public void clickLogin() {
        clickOnElement(loginButton);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // ---- Assertions ----

    public void verifyProductsPageVisible() {
        verify(isElementPresent(productsTitle), "Products page is not visible after login");
    }

    public void verifyErrorVisible() {
        verify(isElementPresent(errorMessage), "Error message is not visible after invalid login");
    }
}
