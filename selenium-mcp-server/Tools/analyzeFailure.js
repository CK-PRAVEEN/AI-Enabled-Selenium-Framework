import fetch from "node-fetch";

const OLLAMA_URL = "http://localhost:11434/api/generate";
const MODEL = "llama3.2";

export async function analyzeFailure(testName, errorMessage) {
    try {
        const prompt = `
You are a Selenium test automation expert.

A TestNG test has failed. Analyze the failure and suggest a fix.

Test Name: ${testName}

Error Message:
${errorMessage}

Please provide:
1. Root cause of the failure
2. Which part of the code likely caused it
3. Exact fix with code example
4. How to prevent this in future

Keep the response concise and practical.
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
