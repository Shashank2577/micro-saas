import { useState } from "react";
import axios from "axios";

export default function ScenarioModeler({ goalId }: { goalId: string }) {
  const [name, setName] = useState("");
  const [returnRate, setReturnRate] = useState(0.06);
  const [inflation, setInflation] = useState(0.02);
  const [contribution, setContribution] = useState(500);
  const [result, setResult] = useState<any>(null);
  const [loading, setLoading] = useState(false);

  const runSimulation = async () => {
    setLoading(true);
    try {
      const headers = { "X-Tenant-ID": "tenant-1" };
      // 1. Create Scenario
      const scenarioRes = await axios.post("http://localhost:8202/api/planning/scenarios", {
        goalId,
        name,
        assumedReturnRate: returnRate,
        inflationRate: inflation,
        monthlyContribution: contribution
      }, { headers });

      // 2. Run Monte Carlo
      const mcRes = await axios.post("http://localhost:8202/api/planning/monte-carlo", {
        scenarioId: scenarioRes.data.id
      }, { headers });

      setResult(mcRes.data);
    } catch (e) {
      console.error(e);
      alert("Simulation failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow border border-gray-200 mt-4">
      <h3 className="font-semibold text-lg mb-4 text-gray-800">Run Monte Carlo Simulation</h3>
      <div className="space-y-4">
        <div>
          <label className="text-sm font-medium text-gray-700">Scenario Name</label>
          <input value={name} onChange={e => setName(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2 text-sm" />
        </div>
        <div className="grid grid-cols-3 gap-4">
          <div>
            <label className="text-sm font-medium text-gray-700">Return Rate</label>
            <input type="number" step="0.01" value={returnRate} onChange={e => setReturnRate(Number(e.target.value))} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2 text-sm" />
          </div>
          <div>
            <label className="text-sm font-medium text-gray-700">Inflation</label>
            <input type="number" step="0.01" value={inflation} onChange={e => setInflation(Number(e.target.value))} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2 text-sm" />
          </div>
          <div>
            <label className="text-sm font-medium text-gray-700">Monthly Addition</label>
            <input type="number" value={contribution} onChange={e => setContribution(Number(e.target.value))} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2 text-sm" />
          </div>
        </div>
        <button onClick={runSimulation} disabled={loading || !goalId} className="w-full bg-blue-600 hover:bg-blue-700 text-white p-2 rounded-md font-medium transition-colors">
          {loading ? "Simulating 1000 paths..." : "Run Simulation"}
        </button>
      </div>

      {result && (
        <div className="mt-6 p-4 bg-gray-50 rounded border border-gray-200">
          <h4 className="font-semibold text-gray-800 mb-2">Results</h4>
          <p className="text-sm">Success Probability: <span className="font-bold text-green-600">{(result.successProbability * 100).toFixed(1)}%</span></p>
          <p className="text-sm">Median Ending Balance: <span className="font-bold">${result.medianEndingBalance}</span></p>
          <p className="text-sm text-gray-500">Best Case (95th): ${result.bestCaseBalance} | Worst Case (5th): ${result.worstCaseBalance}</p>
        </div>
      )}
    </div>
  );
}
