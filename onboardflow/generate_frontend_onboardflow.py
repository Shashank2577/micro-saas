import os
import json

base_dir = "frontend"

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

os.makedirs(f"{base_dir}/src/app", exist_ok=True)
os.makedirs(f"{base_dir}/src/components", exist_ok=True)
os.makedirs(f"{base_dir}/src/lib", exist_ok=True)

# API client
api_client_content = """export const fetchApi = async (path: string, options: RequestInit = {}) => {
  const tenantId = '00000000-0000-0000-0000-000000000001';
  const headers = {
    'Content-Type': 'application/json',
    'X-Tenant-ID': tenantId,
    ...options.headers,
  };
  const response = await fetch(`/api/v1/onboardflow/${path}`, { ...options, headers });
  if (!response.ok) {
    throw new Error(`API error: ${response.statusText}`);
  }
  return response.json();
};
"""
with open(f"{base_dir}/src/lib/api.ts", "w") as f:
    f.write(api_client_content)


for api_path, component_name in pages:
    # Component
    component_content = f"""'use client';

import React, {{ useEffect, useState }} from 'react';
import {{ fetchApi }} from '../lib/api';

export default function {component_name}List() {{
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {{
    fetchApi('{api_path}')
      .then(setData)
      .catch(console.error)
      .finally(() => setLoading(false));
  }}, []);

  if (loading) return <div>Loading {component_name}...</div>;

  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold mb-4">{component_name}</h2>
      <ul className="space-y-2">
        {{data.map((item: any) => (
          <li key={{item.id}} className="p-4 border rounded shadow-sm flex justify-between items-center">
            <span className="font-medium">{{item.name || item.id}}</span>
            <span className="px-2 py-1 bg-gray-100 rounded text-sm">{{item.status}}</span>
          </li>
        ))}}
      </ul>
      {{data.length === 0 && <p>No {component_name} found.</p>}}
    </div>
  );
}}
"""
    with open(f"{base_dir}/src/components/{component_name}List.tsx", "w") as f:
        f.write(component_content)

    # Page
    os.makedirs(f"{base_dir}/src/app/{api_path}", exist_ok=True)
    page_content = f"""import {component_name}List from '../../components/{component_name}List';

export default function {component_name}Page() {{
  return (
    <main className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">{component_name} Management</h1>
      <{component_name}List />
    </main>
  );
}}
"""
    with open(f"{base_dir}/src/app/{api_path}/page.tsx", "w") as f:
        f.write(page_content)
