'use client'

import { useState, useEffect } from 'react'

export default function PerformanceTrendsPage() {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('/api/v1/people-analytics/performance-trends', {
          headers: { 'X-Tenant-ID': 'test-tenant-123' }
        })
        const result = await response.json()
        setData(result)
      } catch (error) {
        console.error('Error fetching performance trends:', error)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  if (loading) return <div data-testid="loading">Loading...</div>

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Performance Trends</h1>
      <div data-testid="performance-trends-list">
        {data.length === 0 ? (
          <p>No trends found.</p>
        ) : (
          <ul>
            {data.map((item: any) => (
              <li key={item.id} className="mb-2 p-4 border rounded">
                {item.name} - {item.status}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  )
}
