"use client";

import React, { useState } from 'react';

interface ResearchQueryFormProps {
  onSubmit: (data: { text: string; jurisdiction: string; practiceArea: string }) => void;
}

export default function ResearchQueryForm({ onSubmit }: ResearchQueryFormProps) {
  const [text, setText] = useState('');
  const [jurisdiction, setJurisdiction] = useState('');
  const [practiceArea, setPracticeArea] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({ text, jurisdiction, practiceArea });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 bg-white p-6 rounded-lg shadow">
      <div>
        <label htmlFor="text" className="block text-sm font-medium text-gray-700">Query Text</label>
        <textarea
          id="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
          rows={4}
          required
        />
      </div>
      <div>
        <label htmlFor="jurisdiction" className="block text-sm font-medium text-gray-700">Jurisdiction</label>
        <select
          id="jurisdiction"
          value={jurisdiction}
          onChange={(e) => setJurisdiction(e.target.value)}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
          required
        >
          <option value="" disabled>Select Jurisdiction</option>
          <option value="federal">Federal</option>
          <option value="state">State</option>
          <option value="international">International</option>
        </select>
      </div>
      <div>
        <label htmlFor="practiceArea" className="block text-sm font-medium text-gray-700">Practice Area</label>
        <select
          id="practiceArea"
          value={practiceArea}
          onChange={(e) => setPracticeArea(e.target.value)}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
          required
        >
          <option value="" disabled>Select Practice Area</option>
          <option value="corporate">Corporate</option>
          <option value="litigation">Litigation</option>
          <option value="tax">Tax</option>
          <option value="ip">Intellectual Property</option>
        </select>
      </div>
      <button
        type="submit"
        className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        Submit Query
      </button>
    </form>
  );
}
