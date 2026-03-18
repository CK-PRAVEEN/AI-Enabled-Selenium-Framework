package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.saucedemo.base.BaseTest;
import com.saucedemo.utils.ExtentManager;
import com.saucedemo.utils.ScreenshotUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestNGListener implements ITestListener {
    private static final ExtentReports EXTENT = ExtentManager.getExtent();
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ExtentTest test = (description == null || description.isBlank())
                ? EXTENT.createTest(name)
                : EXTENT.createTest(name, description);
        TEST.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TEST.get().pass("PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = TEST.get();
        test.fail(result.getThrowable());

        try {
            String path = ScreenshotUtils.takeScreenshot(BaseTest.getDriver(), result.getMethod().getMethodName());
            if (path != null) {
                test.fail("Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            }
        } catch (Exception e) {
            test.warning("Could not capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        TEST.get().skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            EXTENT.flush();
        } finally {
            TEST.remove();
        }
    }
}
