import fetch from "node-fetch";

const OLLAMA_URL = "http://localhost:11434/api/generate";
const MODEL = "llama3.2";

export async function suggestLocatorFix(oldLocator, pageSource) {
    try {
        // Trim page source to avoid token limit
        const trimmedSource = pageSource.length > 3000
            ? pageSource.substring(0, 3000) + "\n... (truncated)"
            : pageSource;

        const prompt = `
You are a Selenium automation expert specializing in locator strategies.

The following Selenium locator is broken and no longer finds the element:

Old Locator: ${oldLocator}

Current Page HTML (partial):
${trimmedSource}

Please provide:
1. Why the old locator is broken
2. Best replacement locator using one of: By.id, By.cssSelector, By.xpath, By.name
3. Two alternative locators as backup
4. Java code snippet showing the fix

Prefer id > cssSelector > xpath in that order.
Keep response concise and practical.
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
