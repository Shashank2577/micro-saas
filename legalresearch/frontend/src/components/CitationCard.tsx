"use client";

import React, { useState } from 'react';

interface CitationCardProps {
  citation: {
    id: string;
    text: string;
    sourceType: string;
    court?: string;
    year?: number;
  };
}

export default function CitationCard({ citation }: CitationCardProps) {
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(citation.text);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="bg-white p-4 rounded-lg shadow border border-gray-200">
      <div className="flex justify-between items-start mb-2">
        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 uppercase tracking-wide">
          {citation.sourceType}
        </span>
        <button
          onClick={handleCopy}
          className="text-gray-400 hover:text-gray-600 focus:outline-none"
          title="Copy to clipboard"
        >
          {copied ? (
            <span className="text-green-600 text-xs">Copied!</span>
          ) : (
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path d="M8 3a1 1 0 011-1h2a1 1 0 110 2H9a1 1 0 01-1-1z" />
              <path d="M6 3a2 2 0 00-2 2v11a2 2 0 002 2h8a2 2 0 002-2V5a2 2 0 00-2-2 3 3 0 01-3 3H9a3 3 0 01-3-3z" />
            </svg>
          )}
        </button>
      </div>
      <p className="text-gray-900 font-medium mb-1">{citation.text}</p>
      {(citation.court || citation.year) && (
        <p className="text-sm text-gray-500">
          {citation.court && <span>{citation.court}</span>}
          {citation.court && citation.year && <span> &bull; </span>}
          {citation.year && <span>{citation.year}</span>}
        </p>
      )}
    </div>
  );
}
