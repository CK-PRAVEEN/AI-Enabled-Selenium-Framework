import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { getFailedTests } from "./Tools/getFailedTests.js";
import { analyzeFailure } from "./Tools/analyzeFailure.js";
import { suggestLocatorFix } from "./Tools/suggestLocatorFix.js";
import { generateTestCase } from "./Tools/generateTestCase.js";
import { z } from "zod";

const server = new McpServer({
    name: "selenium-mcp-server",
    version: "1.0.0"
});

// Tool 1 — Get Failed Tests
server.tool(
    "getFailedTests",
    { reportPath: z.string() },
    async ({ reportPath }) => {
        const result = await getFailedTests(reportPath);
        return { content: [{ type: "text", text: result }] };
    }
);

// Tool 2 — Analyze Failure
server.tool(
    "analyzeFailure",
    { testName: z.string(), errorMessage: z.string() },
    async ({ testName, errorMessage }) => {
        const result = await analyzeFailure(testName, errorMessage);
        return { content: [{ type: "text", text: result }] };
    }
);

// Tool 3 — Suggest Locator Fix
server.tool(
    "suggestLocatorFix",
    { oldLocator: z.string(), pageSource: z.string() },
    async ({ oldLocator, pageSource }) => {
        const result = await suggestLocatorFix(oldLocator, pageSource);
        return { content: [{ type: "text", text: result }] };
    }
);

// Tool 4 — Generate Test Case
server.tool(
    "generateTestCase",
    { description: z.string() },
    async ({ description }) => {
        const result = await generateTestCase(description);
        return { content: [{ type: "text", text: result }] };
    }
);

// Start server
const transport = new StdioServerTransport();
await server.connect(transport);
console.error("Selenium MCP Server running...");