# AI-Enabled Selenium TestNG Framework

A production-grade Selenium automation framework integrated with a custom MCP (Model Context Protocol) server powered by a local LLM (llama3.2 via Ollama) — enabling AI-assisted test generation, failure analysis, and locator fix suggestions **without sending any data outside your network**.

---

## Why This Framework?

Most AI coding tools like Copilot or Cursor send your code and test data to external cloud APIs. In enterprise environments (banking, telecom, healthcare), test failures often contain sensitive production data — customer IDs, order numbers, PII — which cannot leave the network.

This framework solves that by running the AI model **locally via Ollama**, keeping all data on-premise while still getting intelligent test automation assistance.

---

## Architecture

```
Your Selenium Tests (Java)
          ↓
    BaseTest + LoginPage
    ExtentReports + Screenshots
          ↓
    Test Fails
          ↓
    MCP Server (Node.js)
    exposes 4 AI tools
          ↓
    Ollama (localhost:11434)
    llama3.2 — runs fully offline
          ↓
    AI Response
    (never leaves your machine)
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Test Framework | TestNG |
| Browser Automation | Selenium WebDriver 4 |
| Build Tool | Maven |
| Reporting | ExtentReports (Spark) |
| Driver Management | WebDriverManager |
| MCP Server | Node.js + @modelcontextprotocol/sdk |
| Local LLM | Ollama + llama3.2 |
| AI Integration | Claude Desktop (MCP Client) |

---

## Framework Structure

```
AI_ENABLED_PROJECT/
├── src/
│   ├── main/java/com/saucedemo/
│   │   └── utils/
│   │       ├── ConfigReader.java       # Reads config.properties
│   │       ├── WaitUtils.java          # Centralized WebDriverWait
│   │       └── ScreenshotUtils.java    # Auto screenshot on failure
│   └── test/java/com/saucedemo/
│       ├── base/
│       │   ├── BasePage.java           # verify() method for assertions
│       │   └── BaseTest.java           # ThreadLocal WebDriver, setup/teardown
│       ├── listeners/
│       │   └── ExtentTestNGListener.java  # Auto pass/fail/screenshot logging
│       ├── pages/
│       │   └── LoginPage.java          # Page Object with enterText/clickOnElement helpers
│       └── tests/
│           └── LoginTest.java          # Test classes
├── selenium-mcp-server/                # Custom MCP Server
│   ├── index.js                        # MCP server entry point
│   └── Tools/
│       ├── getFailedTests.js           # Parses ExtentReport HTML
│       ├── analyzeFailure.js           # AI analyses test failure
│       ├── suggestLocatorFix.js        # AI suggests new locator from page HTML
│       └── generateTestCase.js         # AI generates TestNG test method
├── src/test/resources/
│   ├── config.properties               # Browser, URL, timeout config
│   └── healenium.properties            # Healenium config (optional)
├── testng.xml                          # Test suite configuration
└── pom.xml                             # Maven dependencies
```

---

## Key Design Decisions

**ThreadLocal WebDriver** — Each thread gets its own WebDriver instance. Safe for parallel test execution with no driver conflicts.

**BasePage.verify()** — All assertions go through a single method that calls `Assert.fail(message)` with a meaningful message. If it fails, the exact reason appears in ExtentReports automatically.

**Private helpers in Page classes** — `enterText(By, String)` and `clickOnElement(By)` own the wait + action logic. Page action methods like `enterUsername()` stay clean and readable.

**Listener-driven reporting** — `ExtentTestNGListener` implements `ITestListener`. Pass/fail/skip/screenshot are logged automatically — no assertion code in tests needed.

---

## MCP Server — 4 AI Tools

| Tool | Input | What AI Does |
|---|---|---|
| `getFailedTests` | ExtentReport HTML path | Parses and returns all failed test names and errors |
| `analyzeFailure` | Test name + error message | Root cause analysis + exact fix with code example |
| `suggestLocatorFix` | Old locator + page HTML | Finds best replacement locator (id > css > xpath) |
| `generateTestCase` | Plain English description | Generates complete TestNG test method for your framework |

---

## Prerequisites

- Java 17+
- Maven 3.9+
- Node.js 18+
- Chrome browser
- [Ollama](https://ollama.com/download) installed
- [Claude Desktop](https://claude.ai/download) (for MCP client)

---

## Setup & Run

### 1. Clone the repo
```bash
git clone https://github.com/CK-PRAVEEN/AI-Enabled-Selenium-Framework.git
cd AI-Enabled-Selenium-Framework
```

### 2. Configure
Create `src/test/resources/config.properties`:
```properties
browser=chrome
baseUrl=https://www.saucedemo.com
explicitWait=15
implicitWait=0
username=standard_user
password=secret_sauce
```

### 3. Run tests
```bash
mvn test
```

ExtentReport generated at `test-output/ExtentReport.html`

---

## MCP Server Setup

### 1. Pull the AI model (one time)
```bash
ollama pull llama3.2
```

### 2. Start Ollama
```bash
ollama serve
```

### 3. Install MCP server dependencies
```bash
cd selenium-mcp-server
npm install
```

### 4. Connect to Claude Desktop
Add to `%APPDATA%\Claude\claude_desktop_config.json`:
```json
{
  "mcpServers": {
    "selenium-mcp-server": {
      "command": "C:\\Program Files\\nodejs\\node.exe",
      "args": ["PATH_TO_PROJECT\\selenium-mcp-server\\index.js"]
    }
  }
}
```

Restart Claude Desktop. You should see `selenium-mcp-server — 4 tools enabled`.

---

## Using the AI Tools

Once connected, type in Claude Desktop chat:

**Generate a test case:**
```
Generate a test case for invalid login on SauceDemo
```

**Analyse a failure:**
```
Analyze this failure: NoSuchElementException on login-button locator
```

**Fix a broken locator:**
```
The locator By.id("login-button") is broken. Here is the page HTML: <paste HTML>
```

---

## Why Ollama and Not Claude API?

In enterprise environments, test failures often contain sensitive production data — customer IDs, PII, financial data. Sending this to an external API violates data compliance policies (GDPR, SOC2, internal security policies).

Ollama runs **100% locally** — no internet required after the initial model download. All data stays inside your machine or private server. This makes it suitable for air-gapped environments like banking, telecom, and government projects.

---

## Parallel Execution

The framework is ready for parallel execution. Change `testng.xml`:
```xml
<suite name="SauceDemo Suite" parallel="methods" thread-count="3">
```

No code changes needed — ThreadLocal handles driver isolation automatically.

---

## Author

**CK Praveen** — SDET with 3.4 years experience  
Stack: Java, Selenium, TestNG, Cucumber BDD, RestAssured, Jenkins, Maven  
[GitHub](https://github.com/CK-PRAVEEN)