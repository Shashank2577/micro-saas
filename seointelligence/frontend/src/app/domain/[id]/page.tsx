"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { ArrowLeftIcon, PlayIcon, FileTextIcon } from "lucide-react";

type ContentGap = {
  id: string;
  topic: string;
  competitorUrl: string;
  estimatedVolume: number;
  effortScore: number;
  priorityScore: number;
  status: string;
};

type RankingAlert = {
  id: string;
  keywordId: string;
  previousRank: number;
  currentRank: number;
  changePct: number;
  alertedAt: string;
};

type ContentBrief = {
  id: string;
  keyword: string;
  titleSuggestion: string;
  outline: string;
  estimatedRankProbability: number;
};

export default function DomainDetails() {
  const params = useParams();
  const router = useRouter();
  const domainId = params.id as string;

  const [gaps, setGaps] = useState<ContentGap[]>([]);
  const [alerts, setAlerts] = useState<RankingAlert[]>([]);
  const [briefs, setBriefs] = useState<Record<string, ContentBrief>>({});
  const [analyzing, setAnalyzing] = useState(false);

  const fetchDetails = async () => {
    try {
      const headers = { "X-Tenant-ID": "00000000-0000-0000-0000-000000000001" };

      const [gapsRes, alertsRes] = await Promise.all([
        fetch(`http://localhost:8133/api/v1/domains/${domainId}/gaps`, { headers }),
        fetch(`http://localhost:8133/api/v1/domains/${domainId}/alerts`, { headers }),
      ]);

      if (gapsRes.ok) setGaps(await gapsRes.json());
      if (alertsRes.ok) setAlerts(await alertsRes.json());
    } catch (error) {
      console.error("Failed to fetch details", error);
    }
  };

  useEffect(() => {
    if (domainId) fetchDetails();
  }, [domainId]);

  const triggerAnalysis = async () => {
    setAnalyzing(true);
    try {
      await fetch(`http://localhost:8133/api/v1/domains/${domainId}/analyze`, {
        method: "POST",
        headers: { "X-Tenant-ID": "00000000-0000-0000-0000-000000000001" },
      });
      setTimeout(fetchDetails, 1000);
    } catch (error) {
      console.error("Failed to trigger analysis", error);
    } finally {
      setAnalyzing(false);
    }
  };

  const generateBrief = async (gapId: string) => {
    try {
      const res = await fetch(`http://localhost:8133/api/v1/gaps/${gapId}/brief`, {
        method: "POST",
        headers: { "X-Tenant-ID": "00000000-0000-0000-0000-000000000001" },
      });
      if (res.ok) {
        const brief = await res.json();
        setBriefs(prev => ({ ...prev, [gapId]: brief }));
      }
    } catch (error) {
      console.error("Failed to generate brief", error);
    }
  };

  return (
    <div className="p-8 max-w-6xl mx-auto">
      <div className="mb-6 flex items-center gap-4">
        <button onClick={() => router.push("/")} className="text-gray-500 hover:text-gray-900">
          <ArrowLeftIcon className="w-6 h-6" />
        </button>
        <h1 className="text-3xl font-bold text-gray-900">Domain Details</h1>
        <div className="ml-auto flex gap-3">
          <button
            onClick={triggerAnalysis}
            disabled={analyzing}
            className="bg-purple-600 text-white px-4 py-2 rounded-md hover:bg-purple-700 flex items-center gap-2 disabled:opacity-50"
          >
            <PlayIcon className="w-4 h-4" />
            {analyzing ? "Analyzing..." : "Trigger AI Analysis"}
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-bold mb-4 text-gray-900">Content Gaps</h2>
          {gaps.length === 0 ? (
            <p className="text-gray-500 text-sm">No content gaps identified. Run analysis.</p>
          ) : (
            <div className="space-y-4">
              {gaps.map((gap) => (
                <div key={gap.id} className="border-b pb-4 last:border-0 last:pb-0">
                  <div className="flex justify-between items-start">
                    <div>
                      <h4 className="font-semibold text-lg text-gray-900">{gap.topic}</h4>
                      <p className="text-sm text-gray-500 truncate max-w-xs" title={gap.competitorUrl}>
                        vs {gap.competitorUrl}
                      </p>
                      <div className="flex gap-4 mt-2 text-xs text-gray-600">
                        <span>Vol: {gap.estimatedVolume}</span>
                        <span>Effort: {gap.effortScore}</span>
                        <span>Priority: {gap.priorityScore}</span>
                      </div>
                    </div>
                    <div>
                      {!briefs[gap.id] ? (
                        <button
                          onClick={() => generateBrief(gap.id)}
                          className="bg-indigo-50 text-indigo-600 px-3 py-1 rounded-md hover:bg-indigo-100 flex items-center gap-1 text-sm font-medium"
                        >
                          <FileTextIcon className="w-4 h-4" />
                          AI Brief
                        </button>
                      ) : (
                        <span className="bg-green-100 text-green-800 text-xs px-2 py-1 rounded-md font-medium">
                          Brief Ready
                        </span>
                      )}
                    </div>
                  </div>

                  {briefs[gap.id] && (
                    <div className="mt-4 bg-gray-50 p-4 rounded-md border border-gray-200 text-sm">
                      <h5 className="font-bold mb-1 text-gray-900">{briefs[gap.id].titleSuggestion}</h5>
                      <p className="text-xs text-gray-500 mb-2">
                        Est. Rank Prob: {briefs[gap.id].estimatedRankProbability}%
                      </p>
                      <div className="font-medium mb-1 text-gray-900">Suggested Outline:</div>
                      <ul className="list-disc pl-5 space-y-1 text-gray-700">
                        {JSON.parse(briefs[gap.id].outline || '[]').map((item: string, i: number) => (
                          <li key={i}>{item}</li>
                        ))}
                      </ul>
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-bold mb-4 text-gray-900">Ranking Alerts</h2>
          {alerts.length === 0 ? (
            <p className="text-gray-500 text-sm">No recent ranking alerts.</p>
          ) : (
            <div className="space-y-4">
              {alerts.map((alert) => (
                <div key={alert.id} className="flex justify-between items-center border-b pb-3 last:border-0">
                  <div>
                    <h4 className="font-medium text-gray-900">Keyword ID: {alert.keywordId.slice(0,8)}...</h4>
                    <p className="text-xs text-gray-500">Alerted: {new Date(alert.alertedAt).toLocaleDateString()}</p>
                  </div>
                  <div className="text-right">
                    <div className="flex items-center gap-2">
                      <span className="text-gray-500 line-through text-sm">{alert.previousRank}</span>
                      <span className="text-gray-900 font-bold">{alert.currentRank}</span>
                    </div>
                    <span className={`text-xs font-bold ${alert.changePct > 0 ? 'text-green-600' : 'text-red-600'}`}>
                      {alert.changePct > 0 ? '+' : ''}{alert.changePct}%
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
