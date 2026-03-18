import { generateTestCase } from "./Tools/generateTestCase.js";

const result = await generateTestCase("test login with valid credentials and verify products page is visible");
console.log(result);