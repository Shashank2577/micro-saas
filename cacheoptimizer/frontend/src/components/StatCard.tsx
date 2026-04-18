import React from 'react';

export const StatCard = ({ title, value, subtitle }: { title: string, value: string | number, subtitle?: string }) => (
  <div className="bg-white overflow-hidden rounded-lg shadow px-4 py-5 sm:p-6 ring-1 ring-black ring-opacity-5">
    <dt className="truncate text-sm font-medium text-gray-500">{title}</dt>
    <dd className="mt-1 text-3xl font-semibold tracking-tight text-gray-900">{value}</dd>
    {subtitle && <p className="mt-2 text-sm text-gray-500">{subtitle}</p>}
  </div>
);
