'use client'

import { useEffect, useState } from 'react'
import { api } from '@/lib/api'
import { useParams } from 'next/navigation'
import Link from 'next/link'
import { Project, Interview, Insight, Report } from '@/types'

export default function ProjectDetails() {
  const { id } = useParams()
  const projectId = id as string

  const [project, setProject] = useState<Project | null>(null)
  const [interviews, setInterviews] = useState<Interview[]>([])
  const [insights, setInsights] = useState<Insight[]>([])
  const [reports, setReports] = useState<Report[]>([])
  const [activeTab, setActiveTab] = useState<'interviews' | 'insights' | 'reports'>('interviews')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function loadData() {
      try {
        const [projRes, intRes, insRes, repRes] = await Promise.all([
          api.get(`/api/projects/${projectId}`),
          api.get(`/api/projects/${projectId}/interviews`),
          api.get(`/api/projects/${projectId}/insights`),
          api.get(`/api/projects/${projectId}/reports`)
        ])

        setProject(projRes.data)
        setInterviews(intRes.data)
        setInsights(insRes.data)
        setReports(repRes.data)
      } catch (error) {
        console.error('Failed to load project details', error)
      } finally {
        setLoading(false)
      }
    }
    if (projectId) loadData()
  }, [projectId])

  const handleSynthesize = async () => {
    try {
      const response = await api.post(`/api/projects/${projectId}/synthesize`)
      setInsights(response.data)
      setActiveTab('insights')
    } catch (error) {
      console.error('Failed to synthesize insights', error)
      alert('Failed to synthesize insights. Check console.')
    }
  }

  const handleGenerateReport = async () => {
    try {
      const response = await api.post(`/api/projects/${projectId}/reports`)
      setReports((prev) => [...prev, response.data])
      setActiveTab('reports')
    } catch (error) {
      console.error('Failed to generate report', error)
      alert('Failed to generate report. Check console.')
    }
  }

  if (loading) return <div className="p-8">Loading project details...</div>
  if (!project) return <div className="p-8">Project not found.</div>

  return (
    <div className="p-8 max-w-5xl mx-auto">
      <div className="mb-6 flex items-center justify-between">
        <div>
          <Link href="/" className="text-blue-600 hover:underline mb-2 inline-block">&larr; Back to Projects</Link>
          <h1 className="text-3xl font-bold">{project.name}</h1>
          <p className="text-gray-600 mt-2">{project.description}</p>
        </div>
      </div>

      <div className="flex border-b mb-6">
        <button
          className={`py-2 px-4 font-semibold ${activeTab === 'interviews' ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'}`}
          onClick={() => setActiveTab('interviews')}
        >
          Interviews ({interviews.length})
        </button>
        <button
          className={`py-2 px-4 font-semibold ${activeTab === 'insights' ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'}`}
          onClick={() => setActiveTab('insights')}
        >
          Insights ({insights.length})
        </button>
        <button
          className={`py-2 px-4 font-semibold ${activeTab === 'reports' ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'}`}
          onClick={() => setActiveTab('reports')}
        >
          Reports ({reports.length})
        </button>
      </div>

      <div className="mt-4">
        {activeTab === 'interviews' && (
          <div>
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">Interviews</h2>
              <button onClick={handleSynthesize} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                Synthesize Insights
              </button>
            </div>
            {interviews.length === 0 ? (
              <p className="text-gray-500">No interviews recorded yet.</p>
            ) : (
              <div className="grid gap-4">
                {interviews.map(i => (
                  <div key={i.id} className="border p-4 rounded bg-gray-50">
                    <div className="font-semibold text-lg">{i.participantName}</div>
                    <div className="text-sm text-gray-500 mb-2">{i.participantEmail}</div>
                    <div>Status: <span className="font-bold text-sm bg-gray-200 px-2 py-1 rounded">{i.status}</span></div>
                    {i.transcript && (
                      <div className="mt-4 border-t pt-4 text-sm text-gray-700 max-h-32 overflow-y-auto whitespace-pre-wrap">
                        {i.transcript}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'insights' && (
          <div>
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">AI Synthesized Insights</h2>
              <button onClick={handleGenerateReport} className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
                Generate Report
              </button>
            </div>
            {insights.length === 0 ? (
              <p className="text-gray-500">No insights synthesized yet.</p>
            ) : (
              <div className="grid gap-6">
                {insights.map(insight => (
                  <div key={insight.id} className="border p-6 rounded-lg shadow-sm border-l-4 border-blue-500">
                    <h3 className="text-xl font-bold mb-2">{insight.theme}</h3>
                    <p className="text-gray-700 mb-4">{insight.description}</p>
                    <div className="mb-4">
                      <span className="text-sm font-semibold bg-blue-100 text-blue-800 px-2 py-1 rounded">
                        Confidence: {(insight.confidenceScore * 100).toFixed(0)}%
                      </span>
                    </div>
                    {insight.supportingQuotes && (
                      <div className="bg-gray-100 p-4 rounded text-sm italic">
                        <div className="font-semibold mb-2 not-italic text-gray-600">Supporting Quotes:</div>
                        {JSON.parse(insight.supportingQuotes).map((quote: string, idx: number) => (
                          <div key={idx} className="mb-2">"{quote}"</div>
                        ))}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'reports' && (
          <div>
            <h2 className="text-xl font-bold mb-4">Research Reports</h2>
            {reports.length === 0 ? (
              <p className="text-gray-500">No reports generated yet.</p>
            ) : (
              <div className="grid gap-6">
                {reports.map(report => (
                  <div key={report.id} className="border p-6 rounded-lg shadow">
                    <h3 className="text-xl font-bold mb-4">{report.title}</h3>
                    <div className="prose max-w-none text-sm text-gray-800 whitespace-pre-wrap bg-gray-50 p-4 rounded border">
                      {report.content}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  )
}
