"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const schemas_1 = require("../src/shared/schemas");
describe('SpecSchema Validation', () => {
    it('should pass for a valid spec object', () => {
        const validSpec = {
            title: 'App',
            description: 'Test Description',
            features: ['Feature 1'],
            endpoints: [{ method: 'GET', path: '/test', description: 'Test endpoint' }]
        };
        const result = schemas_1.SpecSchema.safeParse(validSpec);
        expect(result.success).toBe(true);
    });
    it('should fail if missing required fields', () => {
        const invalidSpec = {
            title: 'App'
        };
        const result = schemas_1.SpecSchema.safeParse(invalidSpec);
        expect(result.success).toBe(false);
    });
});
