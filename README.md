# Selenium TestNG Maven Project

This is a minimal Maven-based Java project preconfigured with Selenium WebDriver and TestNG.

## Project Structure

- `pom.xml` – Maven configuration, Selenium and TestNG dependencies
- `src/test/java/com/example/tests/SampleTest.java` – example Selenium + TestNG test
- `testng.xml` – TestNG suite definition used by Maven Surefire

Standard Maven layout:

- `src/main/java` – application code (currently empty)
- `src/test/java` – test code

## Prerequisites

- Java 17 (or compatible JDK)
- Maven 3.8+ installed and on your PATH
- A Chrome browser installed
- ChromeDriver available on your PATH, or set the `webdriver.chrome.driver` system property to the ChromeDriver executable path.

## How to Run Tests

From the project root:

```bash
mvn clean test
```

Maven Surefire is configured to use `testng.xml`, which runs all tests under the `com.example.tests` package.

