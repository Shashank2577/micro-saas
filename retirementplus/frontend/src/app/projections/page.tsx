"use client";

import React, { useEffect, useState } from 'react';
import api from '@/lib/api';

interface ProjectionData {
  lifeExpectancy: number;
  safeWithdrawalRate: number;
  socialSecurityClaimingAge: number;
  estimatedHealthcareCost: number;
  qcdOpportunityAge: number;
  probabilityOfSuccess: number;
  rothConversionAmount: number;
  rmdAmount: number;
  stressTestSurvivalRate: number;
  annuityGuaranteedIncome: number;
  taxStrategyOrder: string;
}

export default function ProjectionsPage() {
  const [data, setData] = useState<ProjectionData | null>(null);
  const [loading, setLoading] = useState(true);
  const userId = '123e4567-e89b-12d3-a456-426614174000'; // Mock user ID

  useEffect(() => {
    const fetchProjections = async () => {
      try {
        const response = await api.post(`/api/projections/analyze/${userId}`);
        setData(response.data);
      } catch (error) {
        console.error('Error fetching projections:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchProjections();
  }, [userId]);

  if (loading) return <div className="p-8 text-center">Analyzing your complex retirement scenario...</div>;
  if (!data) return <div className="p-8 text-center text-red-500">Failed to load projections. Please ensure you have created a profile first.</div>;

  return (
    <div className="container mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6">Comprehensive Retirement Projections</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-lg shadow border border-blue-100">
          <h2 className="text-xl font-semibold mb-2 text-blue-800">Life Expectancy</h2>
          <p className="text-4xl text-blue-600">{data.lifeExpectancy} <span className="text-lg text-gray-500">years</span></p>
        </div>
        
        <div className="bg-white p-6 rounded-lg shadow border border-green-100">
          <h2 className="text-xl font-semibold mb-2 text-green-800">Safe Withdrawal Rate</h2>
          <p className="text-4xl text-green-600">{data.safeWithdrawalRate}%</p>
          <p className="text-sm text-gray-500 mt-2">Required from savings</p>
        </div>

        <div className="bg-white p-6 rounded-lg shadow border border-purple-100">
          <h2 className="text-xl font-semibold mb-2 text-purple-800">Social Security</h2>
          <p className="text-4xl text-purple-600">Age {data.socialSecurityClaimingAge}</p>
          <p className="text-sm text-gray-500 mt-2">Optimal claiming age</p>
        </div>

        <div className="bg-white p-6 rounded-lg shadow border border-red-100">
          <h2 className="text-xl font-semibold mb-2 text-red-800">Healthcare Costs</h2>
          <p className="text-4xl text-red-600">${data.estimatedHealthcareCost.toLocaleString()}</p>
          <p className="text-sm text-gray-500 mt-2">Estimated lifetime cost</p>
        </div>

        <div className="bg-white p-6 rounded-lg shadow border border-orange-100">
          <h2 className="text-xl font-semibold mb-2 text-orange-800">Tax Strategy</h2>
          <p className="text-xl text-orange-600 font-medium">{data.taxStrategyOrder}</p>
          <p className="text-sm text-gray-500 mt-2">Optimal withdrawal sequence</p>
        </div>

        <div className="bg-white p-6 rounded-lg shadow border border-yellow-100">
          <h2 className="text-xl font-semibold mb-2 text-yellow-800">Roth Conversion</h2>
          <p className="text-4xl text-yellow-600">${data.rothConversionAmount.toLocaleString()}</p>
          <p className="text-sm text-gray-500 mt-2">Opportunity in Year 1</p>
        </div>

        <div className="bg-white p-6 rounded-lg shadow border border-teal-100">
          <h2 className="text-xl font-semibold mb-2 text-teal-800">QCD Opportunity</h2>
          <p className="text-4xl text-teal-600">Age {data.qcdOpportunityAge}</p>
          <p className="text-sm text-gray-500 mt-2">Tax-free giving starts</p>
        </div>

        <div className="bg-white p-6 rounded-lg shadow border border-indigo-100">
          <h2 className="text-xl font-semibold mb-2 text-indigo-800">Stress Test (2008)</h2>
          <p className="text-4xl text-indigo-600">{data.stressTestSurvivalRate}%</p>
          <p className="text-sm text-gray-500 mt-2">Portfolio survival probability</p>
        </div>

        {data.annuityGuaranteedIncome > 0 && (
          <div className="bg-white p-6 rounded-lg shadow border border-pink-100">
            <h2 className="text-xl font-semibold mb-2 text-pink-800">Annuity Strategy</h2>
            <p className="text-4xl text-pink-600">${data.annuityGuaranteedIncome.toLocaleString()}/yr</p>
            <p className="text-sm text-gray-500 mt-2">Guaranteed income conversion</p>
          </div>
        )}

        <div className="bg-white p-6 rounded-lg shadow border border-gray-200">
          <h2 className="text-xl font-semibold mb-2 text-gray-800">Plan Success</h2>
          <p className="text-4xl text-gray-600">{data.probabilityOfSuccess}%</p>
          <p className="text-sm text-gray-500 mt-2">Probability of meeting all goals</p>
        </div>

        <div className="bg-white p-6 rounded-lg shadow border border-emerald-100">
          <h2 className="text-xl font-semibold mb-2 text-emerald-800">Est. Initial RMD</h2>
          <p className="text-4xl text-emerald-600">${data.rmdAmount.toLocaleString()}</p>
          <p className="text-sm text-gray-500 mt-2">Estimated Required Minimum Distribution</p>
        </div>

      </div>
    </div>
  );
}
