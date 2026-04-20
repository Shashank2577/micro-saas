import os

base_dir = "frontend/src/components"

pages = [
    ("employees", "Employees"),
    ("workflows", "OnboardingWorkflows"),
    ("tasks", "OnboardingTasks"),
    ("assignments", "TaskAssignments"),
    ("documents", "Documents"),
    ("provisioning", "SystemProvisioningRequests"),
    ("buddy-pairs", "BuddyPairs"),
    ("feedback", "OnboardingFeedback")
]

for api_path, component_name in pages:
    test_content = f"""import React from 'react';
import {{ render, screen, waitFor }} from '@testing-library/react';
import {component_name}List from './{component_name}List';
import {{ fetchApi }} from '../lib/api';
import {{ describe, it, expect, vi }} from 'vitest';

vi.mock('../lib/api', () => ({{
  fetchApi: vi.fn(),
}}));

describe('{component_name}List', () => {{
  it('renders loading state initially', () => {{
    vi.mocked(fetchApi).mockReturnValue(new Promise(() => {{}}));
    render(<{component_name}List />);
    expect(screen.getByText('Loading {component_name}...')).toBeDefined();
  }});

  it('renders data when loaded', async () => {{
    const mockData = [{{ id: '1', name: 'Test 1', status: 'ACTIVE' }}];
    vi.mocked(fetchApi).mockResolvedValue(mockData);

    render(<{component_name}List />);

    await waitFor(() => {{
      expect(screen.getByText('Test 1')).toBeDefined();
      expect(screen.getByText('ACTIVE')).toBeDefined();
    }});
  }});
}});
"""
    with open(f"{base_dir}/{component_name}List.test.tsx", "w") as f:
        f.write(test_content)
