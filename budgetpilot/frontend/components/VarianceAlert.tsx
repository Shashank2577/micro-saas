import React from 'react';
import { AlertCircle } from 'lucide-react';

export function VarianceAlert({ explanation }: { explanation: string }) {
  return (
    <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded relative flex items-start" role="alert">
      <AlertCircle className="w-5 h-5 mr-2 mt-0.5" />
      <div>
        <strong className="font-bold">High Variance Detected!</strong>
        <span className="block sm:inline ml-2">{explanation}</span>
      </div>
    </div>
  );
}
