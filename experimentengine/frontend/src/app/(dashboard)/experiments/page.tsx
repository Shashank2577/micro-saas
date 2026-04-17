'use client'
import { useEffect, useState } from 'react'
import Link from 'next/link'

export default function ExperimentsPage() {
  const [experiments, setExperiments] = useState<any[]>([])

  useEffect(() => {
    fetch('/api/experiments', { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }})
      .then(res => res.json())
      .then(data => {
        if(Array.isArray(data)) setExperiments(data)
      })
      .catch(console.error)
  }, [])

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-semibold text-gray-900">Experiments</h1>
        <Link href="/experiments/new" className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
          New Experiment
        </Link>
      </div>

      <div className="bg-white shadow rounded-lg">
        <ul className="divide-y divide-gray-200">
          {experiments.length === 0 && <li className="p-4 text-gray-500 text-center">No experiments found.</li>}
          {experiments.map(exp => (
            <li key={exp.id} className="p-4 hover:bg-gray-50 flex justify-between items-center">
              <div>
                <Link href={`/experiments/${exp.id}`} className="text-lg font-medium text-blue-600 hover:underline">
                  {exp.name}
                </Link>
                <p className="text-sm text-gray-500">{exp.hypothesis}</p>
              </div>
              <div>
                <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                  exp.status === 'RUNNING' ? 'bg-green-100 text-green-800' :
                  exp.status === 'CONCLUDED' ? 'bg-gray-100 text-gray-800' :
                  'bg-yellow-100 text-yellow-800'
                }`}>
                  {exp.status}
                </span>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  )
}
