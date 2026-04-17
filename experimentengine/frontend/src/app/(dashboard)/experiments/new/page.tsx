'use client'
import { useState } from 'react'
import { useRouter } from 'next/navigation'

export default function NewExperimentPage() {
  const router = useRouter()
  const [name, setName] = useState('')
  const [hypothesis, setHypothesis] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    const res = await fetch('/api/experiments', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Tenant-ID': '00000000-0000-0000-0000-000000000000'
      },
      body: JSON.stringify({ name, hypothesis })
    })

    if (res.ok) {
      const data = await res.json()
      router.push(`/experiments/${data.id}`)
    }
  }

  return (
    <div className="max-w-2xl mx-auto">
      <h1 className="text-2xl font-semibold text-gray-900 mb-6">Create New Experiment</h1>

      <form onSubmit={handleSubmit} className="bg-white p-6 shadow rounded-lg space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">Name</label>
          <input
            type="text"
            required
            value={name}
            onChange={e => setName(e.target.value)}
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700">Hypothesis</label>
          <textarea
            required
            rows={4}
            value={hypothesis}
            onChange={e => setHypothesis(e.target.value)}
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border"
          />
        </div>

        <div className="flex justify-end pt-4">
          <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
            Create
          </button>
        </div>
      </form>
    </div>
  )
}
