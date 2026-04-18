import { render, screen, fireEvent } from '@testing-library/react';
import { PolicyForm } from '../../components/PolicyForm';
import { vi, describe, it, expect } from 'vitest';
import * as apiModule from '../../api/api';

vi.mock('../../api/api', () => ({
  api: {
    createPolicy: vi.fn(),
  },
}));

describe('PolicyForm', () => {
  it('submits policy data successfully', async () => {
    const onSuccess = vi.fn();
    (apiModule.api.createPolicy as any).mockResolvedValueOnce({});
    
    render(<PolicyForm onSuccess={onSuccess} />);
    
    const appNameInput = screen.getByText('App Name').nextElementSibling as HTMLInputElement;
    fireEvent.change(appNameInput, { target: { value: 'test-app' } });
    
    const nsInput = screen.getByText('Namespace').nextElementSibling as HTMLInputElement;
    fireEvent.change(nsInput, { target: { value: 'test-ns' } });
    
    const btn = screen.getByText('Create Policy');
    fireEvent.click(btn);
    
    // Using a tiny timeout to let the async handle finish
    await new Promise((r) => setTimeout(r, 0));
    
    expect(apiModule.api.createPolicy).toHaveBeenCalledWith(expect.objectContaining({
      appName: 'test-app',
      namespace: 'test-ns',
    }));
    expect(onSuccess).toHaveBeenCalled();
  });
});
