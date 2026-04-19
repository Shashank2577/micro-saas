'use client'

import { useState, useEffect } from 'react'

export default function OrgSnapshotsPage() {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(true)
  const [name, setName] = useState('')
  const [status, setStatus] = useState('ACTIVE')

  const fetchData = async () => {
    try {
      setLoading(true)
      const response = await fetch('/api/v1/people-analytics/org-snapshots', {
        headers: { 'X-Tenant-ID': 'test-tenant-123' }
      })
      const result = await response.json()
      setData(result)
    } catch (error) {
      console.error('Error fetching org snapshots:', error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchData()
  }, [])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await fetch('/api/v1/people-analytics/org-snapshots', {
        method: 'POST',
        headers: {
          'X-Tenant-ID': 'test-tenant-123',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, status, metadataJson: "{}" })
      })
      setName('')
      fetchData()
    } catch (error) {
      console.error('Error creating snapshot:', error)
    }
  }

  if (loading) return <div data-testid="loading">Loading...</div>

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Org Snapshots</h1>

      <form onSubmit={handleSubmit} className="mb-8 p-4 border rounded" data-testid="create-form">
        <h2 className="text-xl mb-2">Create New Snapshot</h2>
        <div className="mb-2">
          <label className="block mb-1">Name</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="border p-2 w-full"
            required
            data-testid="input-name"
          />
        </div>
        <div className="mb-2">
          <label className="block mb-1">Status</label>
          <select
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            className="border p-2 w-full"
            data-testid="select-status"
          >
            <option value="ACTIVE">ACTIVE</option>
            <option value="ARCHIVED">ARCHIVED</option>
          </select>
        </div>
        <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded" data-testid="submit-btn">
          Create
        </button>
      </form>

      <div data-testid="org-snapshots-list">
        {data.length === 0 ? (
          <p>No snapshots found.</p>
        ) : (
          <ul>
            {data.map((item: any) => (
              <li key={item.id} className="mb-2 p-4 border rounded">
                <strong>{item.name}</strong> - {item.status}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  )
}
