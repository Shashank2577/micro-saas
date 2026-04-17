import { SpecSchema } from '../src/shared/schemas';

describe('SpecSchema Validation', () => {
  it('should pass for a valid spec object', () => {
    const validSpec = {
      title: 'App',
      description: 'Test Description',
      features: ['Feature 1'],
      endpoints: [{ method: 'GET', path: '/test', description: 'Test endpoint' }]
    };
    
    const result = SpecSchema.safeParse(validSpec);
    expect(result.success).toBe(true);
  });

  it('should fail if missing required fields', () => {
    const invalidSpec = {
      title: 'App'
    };
    
    const result = SpecSchema.safeParse(invalidSpec);
    expect(result.success).toBe(false);
  });
});
