import fetch from "node-fetch";

const OLLAMA_URL = "http://localhost:11434/api/generate";
const MODEL = "llama3.2";

export async function generateTestCase(description) {
    try {
        const prompt = `
You are a Selenium TestNG automation expert.

Generate a complete TestNG test method based on this description:

${description}

The framework has the following structure:
- BaseTest class with getDriver() static method
- Page classes: LoginPage, ProductsPage
- LoginPage methods: login(username, password), verifyProductsPageVisible(), verifyErrorVisible()
- BasePage has verify(boolean condition, String message) method
- ConfigReader.getOrDefault(key, defaultValue) for config values
- Tests extend BaseTest
- Package: com.saucedemo.tests

Please provide:
1. Complete TestNG test method with @Test annotation
2. Proper test method name following naming convention
3. Comments explaining each step
4. Use existing page classes and methods where possible
5. Include meaningful assertion messages

Return only Java code, no explanation outside the code.
        `;

        const response = await fetch(OLLAMA_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                model: MODEL,
                prompt: prompt,
                stream: false
            })
        });

        if (!response.ok) {
            return `Ollama API error: ${response.status} ${response.statusText}`;
        }

        const data = await response.json();
        return data.response || "No response from model";

    } catch (error) {
        return `Error calling Ollama: ${error.message}`;
    }
}