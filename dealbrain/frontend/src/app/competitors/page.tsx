"use client";

import React from 'react';

export default function CompetitorsPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Competitors Tracking</h1>
      <div className="bg-white p-6 rounded-lg shadow">
        <p className="text-gray-500">Track competitor presence across active deals.</p>
        <ul className="mt-4 space-y-3">
          <li className="border-b pb-2">
            <strong>Acme Corp</strong> - Seen in 3 deals
          </li>
          <li className="border-b pb-2">
            <strong>Globex</strong> - Seen in 1 deal
          </li>
        </ul>
      </div>
    </div>
  );
}
