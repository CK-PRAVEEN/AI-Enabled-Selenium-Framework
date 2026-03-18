package com.saucedemo.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtils {
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {}

    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            Path outDir = Path.of("test-output", "screenshots");
            Files.createDirectories(outDir);

            String safeName = (testName == null || testName.isBlank()) ? "test" : testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String fileName = safeName + "_" + LocalDateTime.now().format(TS) + ".png";
            Path outPath = outDir.resolve(fileName);

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, outPath.toFile());
            return outPath.toString();
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }
}
