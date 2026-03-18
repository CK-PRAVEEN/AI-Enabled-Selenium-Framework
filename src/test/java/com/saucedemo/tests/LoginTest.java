package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Login to SauceDemo with valid credentials")
    public void loginWithValidCredentials() {
        getDriver().get(ConfigReader.get("baseUrl"));

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));

        loginPage.verifyProductsPageVisible();
    }

    @Test(description = "Login fails with invalid password")
    public void loginWithInvalidPassword() {
        getDriver().get(ConfigReader.get("baseUrl"));

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(ConfigReader.get("username"), "wrong_password");

        loginPage.verifyErrorVisible();
    }

    @Test(description = "Locked out user cannot login")
    public void loginWithLockedOutUser() {
        getDriver().get(ConfigReader.get("baseUrl"));

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("locked_out_user", ConfigReader.get("password"));

        loginPage.verifyErrorVisible();
    }

    @Test(description = "Login fails when username is empty")
    public void loginWithEmptyUsername() {
        getDriver().get(ConfigReader.get("baseUrl"));

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterPassword(ConfigReader.get("password"));
        loginPage.clickLogin();

        loginPage.verifyErrorVisible();
    }

    @Test(description = "Login fails when password is empty")
    public void loginWithEmptyPassword() {
        getDriver().get(ConfigReader.get("baseUrl"));

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(ConfigReader.get("username"));
        loginPage.clickLogin();

        loginPage.verifyErrorVisible();
    }
}
