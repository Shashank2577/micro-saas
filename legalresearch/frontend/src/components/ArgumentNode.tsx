import React from 'react';

interface ArgumentNodeProps {
  data: {
    id: string;
    label: string;
    type: 'premise' | 'conclusion' | 'counterargument';
    strength?: number;
  };
}

export default function ArgumentNode({ data }: ArgumentNodeProps) {
  const getColors = () => {
    switch (data.type) {
      case 'premise': return 'bg-blue-50 border-blue-200 text-blue-900';
      case 'conclusion': return 'bg-green-50 border-green-200 text-green-900';
      case 'counterargument': return 'bg-red-50 border-red-200 text-red-900';
      default: return 'bg-gray-50 border-gray-200 text-gray-900';
    }
  };

  return (
    <div className={`p-4 rounded-lg border-2 shadow-sm w-64 ${getColors()}`}>
      <div className="flex justify-between items-center mb-2">
        <span className="text-xs font-bold uppercase tracking-wider opacity-75">{data.type}</span>
        {data.strength !== undefined && (
          <span className="text-xs font-medium bg-white px-2 py-1 rounded-full opacity-75">
            Strength: {data.strength * 100}%
          </span>
        )}
      </div>
      <p className="text-sm font-medium leading-relaxed">{data.label}</p>
    </div>
  );
}
