"use client";




import React from 'react';
import Link from 'next/link';
import { AlertCircle, TrendingUp, Activity, Users, Split } from 'lucide-react';

interface InsightCardProps {
  insight: any;
}

export function InsightCard({ insight }: InsightCardProps) {
  const getIcon = (type: string) => {
    switch(type) {
      case 'ANOMALY': return <AlertCircle className="w-5 h-5 text-red-500" />;
      case 'TREND': return <TrendingUp className="w-5 h-5 text-blue-500" />;
      case 'CORRELATION': return <Activity className="w-5 h-5 text-purple-500" />;
      case 'SEGMENT': return <Users className="w-5 h-5 text-green-500" />;
      case 'COMPARISON': return <Split className="w-5 h-5 text-orange-500" />;
      default: return <Activity className="w-5 h-5 text-gray-500" />;
    }
  };

  const getStatusColor = (status: string) => {
    switch(status) {
      case 'NEW': return 'bg-blue-100 text-blue-800';
      case 'ACKNOWLEDGED': return 'bg-yellow-100 text-yellow-800';
      case 'RESOLVED': return 'bg-green-100 text-green-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="bg-white border rounded-lg shadow-sm p-4 hover:shadow-md transition-shadow">
      <div className="flex justify-between items-start mb-2">
        <div className="flex items-center space-x-2">
          {getIcon(insight.type)}
          <span className="text-sm font-semibold text-gray-500">{insight.type}</span>
        </div>
        <span className={`text-xs px-2 py-1 rounded-full ${getStatusColor(insight.status)}`}>
          {insight.status}
        </span>
      </div>
      
      <Link href={`/insights/${insight.id}`} className="block">
        <h3 className="text-lg font-bold text-gray-900 mb-1 hover:text-blue-600">
          {insight.title}
        </h3>
        <p className="text-sm text-gray-600 line-clamp-2">
          {insight.description}
        </p>
      </Link>
      
      <div className="mt-4 flex items-center justify-between text-xs text-gray-500">
        <div>Impact: <span className="font-semibold">{(insight.impactScore * 10).toFixed(1)}/10</span></div>
        <div>{new Date(insight.createdAt).toLocaleDateString()}</div>
      </div>
    </div>
  );
}
