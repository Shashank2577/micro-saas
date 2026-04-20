'use client';
import { useState } from 'react';

export default function TicketDetailPage({ params }: { params: { id: string } }) {
  const [suggestion, setSuggestion] = useState<string | null>(null);

  const handleGenerateSuggestion = () => {
    // Simulating API Call
    setSuggestion("Hello, I understand you are having an issue. We are working on it right now. Could you provide more details?");
  };

  return (
    <div className="flex h-[calc(100vh-80px)] gap-6">
      {/* Ticket Details Panel */}
      <div className="flex-1 flex flex-col space-y-4">
        <div className="bg-white p-6 rounded-lg border shadow-sm flex-1">
          <h1 className="text-2xl font-bold mb-2">Ticket: {params.id}</h1>
          <p className="text-gray-600 mb-6">Customer having issues with login</p>

          <div className="space-y-4">
            <div className="bg-gray-50 p-4 rounded-lg">
              <p className="font-semibold text-sm text-gray-500 mb-1">Customer</p>
              <p>Hi, I am unable to login since this morning. It says invalid password.</p>
            </div>

            {/* Reply box */}
            <div className="mt-8 border-t pt-4">
              <textarea
                className="w-full border rounded-lg p-3 h-32 focus:ring-2 focus:ring-blue-500 outline-none"
                placeholder="Type your reply here..."
                defaultValue={suggestion || ''}
              />
              <div className="flex justify-between mt-2">
                <button className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                  Send Reply
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* AI Suggestion Panel */}
      <div className="w-96 bg-white p-6 rounded-lg border shadow-sm">
        <h2 className="text-lg font-bold mb-4 flex items-center">
          <span className="mr-2">✨</span> AI Co-Pilot
        </h2>

        <div className="space-y-4">
          <button
            onClick={handleGenerateSuggestion}
            className="w-full bg-indigo-50 text-indigo-700 border border-indigo-200 py-2 rounded font-medium hover:bg-indigo-100"
          >
            Generate Suggestion
          </button>

          {suggestion && (
            <div className="bg-gray-50 border p-4 rounded-lg text-sm">
              <p className="text-gray-500 font-semibold mb-2 flex justify-between">
                Suggested Reply
                <span className="text-green-600">88% Match</span>
              </p>
              <p className="text-gray-800">{suggestion}</p>
              <div className="mt-4 flex gap-2">
                <button className="flex-1 bg-white border px-2 py-1 rounded text-gray-600 hover:bg-gray-50">Edit</button>
                <button className="flex-1 bg-indigo-600 text-white px-2 py-1 rounded hover:bg-indigo-700">Insert</button>
              </div>
            </div>
          )}

          <div className="mt-8 border-t pt-4">
            <h3 className="font-semibold text-sm text-gray-700 mb-2">Knowledge Base Articles</h3>
            <ul className="text-sm space-y-2 text-blue-600">
              <li><a href="#" className="hover:underline">Resetting your password</a></li>
              <li><a href="#" className="hover:underline">Login issues troubleshooting</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
