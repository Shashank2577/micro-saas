"use client";

import { useEffect, useState } from "react";
import axios from "axios";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";
import GoalForm from "../components/GoalForm";
import ScenarioModeler from "../components/ScenarioModeler";

export default function Home() {
  const [goals, setGoals] = useState([]);
  const [aiAdvice, setAiAdvice] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showGoalForm, setShowGoalForm] = useState(false);
  const [selectedGoalId, setSelectedGoalId] = useState("");

  useEffect(() => {
    async function fetchData() {
      try {
        const headers = { "X-Tenant-ID": "tenant-1" };
        const [goalsRes, aiRes] = await Promise.all([
          axios.get("http://localhost:8202/api/goals", { headers }),
          axios.get("http://localhost:8202/api/recommendations/ai", { headers }).catch(() => ({ data: { recommendations: ["No AI advice available right now."] } }))
        ]);
        setGoals(goalsRes.data);
        setAiAdvice(aiRes.data.recommendations);
      } catch (error) {
        console.error("Error fetching data", error);
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, []);

  const fetchGoals = async () => {
    try {
      const res = await axios.get("http://localhost:8202/api/goals", { headers: { "X-Tenant-ID": "tenant-1" } });
      setGoals(res.data);
      if (res.data.length > 0 && !selectedGoalId) {
        setSelectedGoalId(res.data[0].id);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const mockNetWorthData = [
    { year: "2020", netWorth: 100000 },
    { year: "2021", netWorth: 120000 },
    { year: "2022", netWorth: 150000 },
    { year: "2023", netWorth: 180000 },
    { year: "2024", netWorth: 210000 },
  ];

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <header className="mb-8">
        <h1 className="text-4xl font-bold text-gray-900">WealthPlan Dashboard</h1>
        <p className="text-gray-600 mt-2">Your AI-powered financial command center.</p>
      </header>

      {loading ? (
        <div className="text-gray-500">Loading your financial data...</div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2 space-y-8">
            <section className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
              <h2 className="text-2xl font-semibold mb-4 text-gray-800">Net Worth Growth</h2>
              <div className="h-72">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart data={mockNetWorthData}>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} />
                    <XAxis dataKey="year" />
                    <YAxis tickFormatter={(val) => `$${val / 1000}k`} />
                    <Tooltip formatter={(val) => `$${val}`} />
                    <Line type="monotone" dataKey="netWorth" stroke="#2563eb" strokeWidth={3} dot={{ r: 4 }} />
                  </LineChart>
                </ResponsiveContainer>
              </div>
            </section>

            <section className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
              <h2 className="text-2xl font-semibold mb-4 text-gray-800">Your Goals</h2>
              {goals.length === 0 ? (
                <p className="text-gray-500">No goals set yet. Create one to get started!</p>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {goals.map((goal: any) => (
                    <div key={goal.id} className="border border-gray-200 rounded-lg p-4">
                      <h3 className="font-semibold text-lg">{goal.name}</h3>
                      <p className="text-sm text-gray-500">{goal.type}</p>
                      <div className="mt-4 flex justify-between text-sm">
                        <span>${goal.currentAmount}</span>
                        <span className="text-gray-400">of ${goal.targetAmount}</span>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-2.5 mt-2">
                        <div className="bg-blue-600 h-2.5 rounded-full" style={{ width: `${Math.min(100, (goal.currentAmount / goal.targetAmount) * 100)}%` }}></div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </section>
          </div>

          <div className="space-y-8">
            <section className="bg-gradient-to-br from-indigo-50 to-blue-50 p-6 rounded-xl shadow-sm border border-indigo-100">
              <h2 className="text-2xl font-semibold mb-4 text-indigo-900 flex items-center">
                <svg className="w-6 h-6 mr-2 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path></svg>
                AI Insights
              </h2>
              <ul className="space-y-3">
                {aiAdvice.map((advice, idx) => (
                  <li key={idx} className="flex text-indigo-800 text-sm">
                    <span className="mr-2">•</span>
                    <span>{advice}</span>
                  </li>
                ))}
              </ul>
            </section>

            <section className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
              <h2 className="text-xl font-semibold mb-4 text-gray-800">Quick Actions</h2>
              <div className="space-y-3">
                <button onClick={() => setShowGoalForm(!showGoalForm)} className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-lg transition-colors">
                  {showGoalForm ? "Cancel" : "Add New Goal"}
                </button>
                <button onClick={() => window.open("http://localhost:8202/api/documents/plan-pdf?tenantId=tenant-1", "_blank")} className="w-full bg-white hover:bg-gray-50 text-gray-700 border border-gray-300 font-medium py-2 px-4 rounded-lg transition-colors">
                  Download Financial Plan
                </button>
              </div>
            </section>

            {showGoalForm && (
              <GoalForm onGoalCreated={() => { setShowGoalForm(false); fetchGoals(); }} />
            )}

            {goals.length > 0 && (
               <ScenarioModeler goalId={selectedGoalId} />
            )}
          </div>
        </div>
      )}
    </div>
  );
}
