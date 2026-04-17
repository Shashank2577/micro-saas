"use client"
import React, { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { FileText, Plus, Calculator, Settings, CheckCircle } from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const mockProjections = [
  { year: '2024', liability: 24000, optimized: 18000 },
  { year: '2025', liability: 26000, optimized: 19500 },
  { year: '2026', liability: 28000, optimized: 21000 },
];

export default function Dashboard() {
  const [activeTab, setActiveTab] = useState('overview');

  return (
    <div className="min-h-screen bg-slate-50 p-8">
      <div className="max-w-7xl mx-auto space-y-8">
        <div className="flex justify-between items-center">
          <h1 className="text-3xl font-bold text-slate-900">Tax Optimizer Dashboard</h1>
          <div className="flex gap-2">
            <Button variant="outline"><Settings className="w-4 h-4 mr-2" /> Settings</Button>
            <Button><Plus className="w-4 h-4 mr-2" /> New Income/Expense</Button>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium text-slate-500">Est. Tax Liability (2024)</CardTitle>
              <Calculator className="w-4 h-4 text-slate-400" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">$24,500</div>
              <p className="text-xs text-slate-500">+$2,000 from last month</p>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium text-slate-500">Potential Savings</CardTitle>
              <CheckCircle className="w-4 h-4 text-green-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-600">$6,000</div>
              <p className="text-xs text-slate-500">2 Opportunities identified</p>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium text-slate-500">Accountant Status</CardTitle>
              <FileText className="w-4 h-4 text-blue-500" />
            </CardHeader>
            <CardContent>
              <div className="text-xl font-bold">Package Ready</div>
              <p className="text-xs text-slate-500">Not yet shared</p>
            </CardContent>
          </Card>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="col-span-1">
            <CardHeader>
              <CardTitle>Tax Projections (3 Years)</CardTitle>
            </CardHeader>
            <CardContent className="h-80">
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={mockProjections}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="year" />
                  <YAxis />
                  <Tooltip />
                  <Area type="monotone" dataKey="liability" stackId="1" stroke="#ef4444" fill="#fca5a5" name="Standard" />
                  <Area type="monotone" dataKey="optimized" stackId="2" stroke="#22c55e" fill="#86efac" name="Optimized" />
                </AreaChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

          <Card className="col-span-1">
            <CardHeader>
              <CardTitle>Optimization Recommendations</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="p-4 border rounded-lg bg-green-50 border-green-100">
                <h3 className="font-semibold text-green-800">Tax Loss Harvesting</h3>
                <p className="text-sm text-green-700 mt-1">Sell tech stocks to harvest $5,000 in losses. Beware 30-day wash sale rule.</p>
                <Button className="mt-3 bg-green-600 hover:bg-green-700" size="sm">Review Details</Button>
              </div>
              <div className="p-4 border rounded-lg bg-blue-50 border-blue-100">
                <h3 className="font-semibold text-blue-800">Charitable Giving Strategy</h3>
                <p className="text-sm text-blue-700 mt-1">Consider a Donor-Advised Fund (DAF) to bunch 2 years of giving.</p>
                <Button className="mt-3 bg-blue-600 hover:bg-blue-700" size="sm">Model Scenario</Button>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
