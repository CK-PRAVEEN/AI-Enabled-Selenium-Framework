package com.saucedemo.utils;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class WaitUtils {
    private WaitUtils() {}

    public static WebDriverWait defaultWait(WebDriver driver) {
        int seconds = ConfigReader.getIntOrDefault("explicitWait", 15);
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }
}
