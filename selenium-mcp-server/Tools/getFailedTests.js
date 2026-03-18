import * as fs from "fs";
import * as cheerio from "cheerio";

export async function getFailedTests(reportPath) {
    try {
        // Read ExtentReport HTML
        if (!fs.existsSync(reportPath)) {
            return `Report not found at path: ${reportPath}`;
        }

        const html = fs.readFileSync(reportPath, "utf-8");
        const $ = cheerio.load(html);

        const failedTests = [];

        // ExtentReport marks failed tests with class "fail"
        $(".test-node.fail, .leaf.fail").each((i, el) => {
            const testName = $(el).find(".test-node-name, .name").first().text().trim();
            const errorMsg = $(el).find(".exception-message, .stacktrace").first().text().trim();

            if (testName) {
                failedTests.push({
                    name: testName,
                    error: errorMsg || "No error message captured"
                });
            }
        });

        if (failedTests.length === 0) {
            return "No failed tests found in the report.";
        }

        // Format result
        let result = `Found ${failedTests.length} failed test(s):\n\n`;
        failedTests.forEach((t, i) => {
            result += `${i + 1}. Test: ${t.name}\n`;
            result += `   Error: ${t.error}\n\n`;
        });

        return result;

    } catch (error) {
        return `Error reading report: ${error.message}`;
    }
}