package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public abstract class BasePage {
    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected void verify(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }
}
