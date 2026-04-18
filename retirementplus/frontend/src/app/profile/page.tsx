"use client";

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import api from '@/lib/api';

export default function ProfilePage() {
  const [formData, setFormData] = useState({
    userId: '123e4567-e89b-12d3-a456-426614174000', // Mock user ID
    currentAge: 60,
    retirementAge: 67,
    currentSavings: 500000.00,
    desiredIncome: 60000.00,
    gender: 'MALE',
    healthStatus: 'HEALTHY',
    familyHistory: 'MODERATE',
    inheritanceGoal: 500000.00,
    wantsAnnuity: false
  });
  const router = useRouter();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target as HTMLInputElement;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : (name === 'currentAge' || name === 'retirementAge' || name === 'currentSavings' || name === 'desiredIncome' || name === 'inheritanceGoal' ? Number(value) : value)
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/api/profiles', formData);
      router.push('/projections');
    } catch (error) {
      console.error('Error saving profile:', error);
    }
  };

  return (
    <div className="container mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6">Retirement Profile</h1>
      <form onSubmit={handleSubmit} className="max-w-xl space-y-4">
        <div>
          <label className="block text-sm font-medium">Current Age</label>
          <input type="number" name="currentAge" value={formData.currentAge} onChange={handleChange} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" required />
        </div>
        <div>
          <label className="block text-sm font-medium">Retirement Age</label>
          <input type="number" name="retirementAge" value={formData.retirementAge} onChange={handleChange} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" required />
        </div>
        <div>
          <label className="block text-sm font-medium">Current Savings ($)</label>
          <input type="number" name="currentSavings" value={formData.currentSavings} onChange={handleChange} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" required />
        </div>
        <div>
          <label className="block text-sm font-medium">Desired Annual Income ($)</label>
          <input type="number" name="desiredIncome" value={formData.desiredIncome} onChange={handleChange} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" required />
        </div>
        <div>
          <label className="block text-sm font-medium">Inheritance Goal ($)</label>
          <input type="number" name="inheritanceGoal" value={formData.inheritanceGoal} onChange={handleChange} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" required />
        </div>
        <div>
          <label className="block text-sm font-medium flex items-center">
            <input type="checkbox" name="wantsAnnuity" checked={formData.wantsAnnuity} onChange={handleChange} className="mr-2 rounded border-gray-300" />
            Consider Annuity for Guaranteed Income
          </label>
        </div>
        <div>
          <label className="block text-sm font-medium">Gender</label>
          <select name="gender" value={formData.gender} onChange={handleChange} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border">
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
            <option value="OTHER">Other</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium">Health Status</label>
          <select name="healthStatus" value={formData.healthStatus} onChange={handleChange} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border">
            <option value="HEALTHY">Healthy</option>
            <option value="AVERAGE">Average</option>
            <option value="POOR">Poor</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium">Family History (Longevity)</label>
          <select name="familyHistory" value={formData.familyHistory} onChange={handleChange} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border">
            <option value="LONG">Long</option>
            <option value="MODERATE">Moderate</option>
            <option value="SHORT">Short</option>
          </select>
        </div>
        <button type="submit" className="w-full bg-blue-600 text-white p-2 rounded-md hover:bg-blue-700">Save Profile & Continue</button>
      </form>
    </div>
  );
}
