"use client";
import React, { useState } from 'react';

export default function PreferencesPage() {
  const [emailOptIn, setEmailOptIn] = useState(true);
  const [smsOptIn, setSmsOptIn] = useState(true);

  const savePreferences = () => {
    // API Call to save
    console.log("Saving preferences", { emailOptIn, smsOptIn });
  };

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Notification Preferences</h1>
      <div className="bg-white p-6 rounded-lg shadow space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="text-lg font-medium">Email Notifications</h3>
            <p className="text-gray-500 text-sm">Receive updates via email</p>
          </div>
          <input
            type="checkbox"
            checked={emailOptIn}
            onChange={(e) => setEmailOptIn(e.target.checked)}
            className="h-5 w-5 text-blue-600"
          />
        </div>
        <div className="flex items-center justify-between">
          <div>
            <h3 className="text-lg font-medium">SMS Notifications</h3>
            <p className="text-gray-500 text-sm">Receive updates via SMS</p>
          </div>
          <input
            type="checkbox"
            checked={smsOptIn}
            onChange={(e) => setSmsOptIn(e.target.checked)}
            className="h-5 w-5 text-blue-600"
          />
        </div>
        <div className="mt-6">
          <button
            onClick={savePreferences}
            className="bg-blue-600 text-white px-4 py-2 rounded"
          >
            Save Preferences
          </button>
        </div>
      </div>
    </div>
  );
}
