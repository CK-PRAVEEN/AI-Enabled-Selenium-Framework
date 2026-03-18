package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExtentManager {
    private static ExtentReports extent;

    private ExtentManager() {}

    public static synchronized ExtentReports getExtent() {
        if (extent != null) return extent;

        try {
            Files.createDirectories(Path.of("test-output"));
        } catch (Exception ignored) {
            // ignore
        }

        String reportPath = Path.of("test-output", "ExtentReport.html").toString();
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("SauceDemo Automation Report");
        spark.config().setReportName("TestNG + Selenium + ExtentReports");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java", System.getProperty("java.version"));
        extent.setSystemInfo("Browser", ConfigReader.getOrDefault("browser", "chrome"));
        return extent;
    }
}
