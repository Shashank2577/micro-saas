import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import NotesPage from './page';
import { api } from '../../lib/api';

jest.mock('../../lib/api', () => ({
  api: {
    generateNote: jest.fn().mockResolvedValue({
      content: 'Generated SOAP note content',
    }),
  },
}));

describe('NotesPage', () => {
  it('handles note generation', async () => {
    render(<NotesPage />);

    const input = screen.getByPlaceholderText('Enter Encounter ID');
    fireEvent.change(input, { target: { value: 'enc-123' } });

    const button = screen.getByText('Generate SOAP Note');
    fireEvent.click(button);

    expect(screen.getByText('Generating...')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText('Generate SOAP Note')).toBeInTheDocument();
    });

    expect(screen.getByDisplayValue('Generated SOAP note content')).toBeInTheDocument();
  });
});
