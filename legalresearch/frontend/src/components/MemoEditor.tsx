"use client";

import React, { useState, useEffect } from 'react';

interface Section {
  id: string;
  title: string;
  content: string;
}

interface MemoEditorProps {
  initialSections: Section[];
  onSave: (sections: Section[]) => void;
}

export default function MemoEditor({ initialSections, onSave }: MemoEditorProps) {
  const [sections, setSections] = useState<Section[]>(initialSections);
  const [saveStatus, setSaveStatus] = useState<string>('');

  useEffect(() => {
    const timer = setTimeout(() => {
      onSave(sections);
      setSaveStatus('Saved at ' + new Date().toLocaleTimeString());
    }, 2000);

    return () => clearTimeout(timer);
  }, [sections, onSave]);

  const handleSectionChange = (index: number, content: string) => {
    const newSections = [...sections];
    newSections[index].content = content;
    setSections(newSections);
    setSaveStatus('Saving...');
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-bold">Memo Editor</h2>
        <span className="text-sm text-gray-500">{saveStatus}</span>
      </div>
      {sections.map((section, index) => (
        <div key={section.id} className="bg-white p-4 rounded-lg shadow border border-gray-200">
          <h3 className="text-lg font-medium mb-2">{section.title}</h3>
          <textarea
            value={section.content}
            onChange={(e) => handleSectionChange(index, e.target.value)}
            className="w-full h-32 p-2 border border-gray-300 rounded-md focus:ring-indigo-500 focus:border-indigo-500"
            placeholder={`Enter content for ${section.title}...`}
          />
        </div>
      ))}
    </div>
  );
}
