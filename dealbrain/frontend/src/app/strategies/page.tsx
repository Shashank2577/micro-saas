"use client";

import React from 'react';

export default function StrategiesPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Sales Strategies</h1>
      <div className="bg-white p-6 rounded-lg shadow">
        <p className="text-gray-500">Active sales plays and strategies.</p>
        <ul className="mt-4 space-y-3">
          <li className="border-b pb-2">
            <strong>Value Based Selling</strong> - Active on 12 deals
          </li>
          <li className="border-b pb-2">
            <strong>Challenger Sale</strong> - Active on 5 deals
          </li>
        </ul>
      </div>
    </div>
  );
}
