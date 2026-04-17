'use client'
import { useEffect, useState } from 'react'
import { useParams } from 'next/navigation'

export default function ExperimentDetailPage() {
  const { id } = useParams()
  const [experiment, setExperiment] = useState<any>(null)

  useEffect(() => {
    fetch(`/api/experiments/${id}`, { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }})
      .then(res => res.json())
      .then(data => setExperiment(data))
      .catch(console.error)
  }, [id])

  if (!experiment) return <div>Loading...</div>

  return (
    <div>
      <div className="flex justify-between items-start mb-6">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">{experiment.name}</h1>
          <p className="text-gray-500 mt-1">{experiment.hypothesis}</p>
        </div>
        <span className={`px-3 py-1 inline-flex text-sm leading-5 font-semibold rounded-full ${
          experiment.status === 'RUNNING' ? 'bg-green-100 text-green-800' :
          experiment.status === 'CONCLUDED' ? 'bg-gray-100 text-gray-800' :
          'bg-yellow-100 text-yellow-800'
        }`}>
          {experiment.status}
        </span>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white p-6 shadow rounded-lg">
          <h2 className="text-lg font-medium mb-4">Details</h2>
          <dl className="space-y-3">
            <div>
              <dt className="text-sm font-medium text-gray-500">Min Sample Size</dt>
              <dd className="text-sm text-gray-900">{experiment.minSampleSize || 'Not set'}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Significance Threshold</dt>
              <dd className="text-sm text-gray-900">{experiment.significanceThreshold || 'Not set'}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Peek Protection</dt>
              <dd className="text-sm text-gray-900">{experiment.peekProtection ? 'Enabled' : 'Disabled'}</dd>
            </div>
          </dl>
        </div>

        <div className="bg-white p-6 shadow rounded-lg">
          <h2 className="text-lg font-medium mb-4">Actions</h2>
          <div className="space-y-3">
            {experiment.status === 'DRAFT' && (
              <button className="w-full px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">
                Start Experiment
              </button>
            )}
            {experiment.status === 'RUNNING' && (
              <>
                <button className="w-full px-4 py-2 bg-yellow-600 text-white rounded-md hover:bg-yellow-700">
                  Pause Experiment
                </button>
                <button className="w-full px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 mt-2">
                  Conclude & Rollout
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
