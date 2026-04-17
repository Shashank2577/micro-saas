'use client'
import { useEffect, useState } from 'react'

export default function FlagsPage() {
  const [flags, setFlags] = useState<any[]>([])
  const [loading, setLoading] = useState(true)

  const fetchFlags = () => {
    fetch('/api/flags', { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }})
      .then(res => res.json())
      .then(data => {
        if(Array.isArray(data)) setFlags(data)
        setLoading(false)
      })
      .catch(err => {
        console.error(err)
        setLoading(false)
      })
  }

  useEffect(() => {
    fetchFlags()
  }, [])

  const toggleFlag = async (key: string, currentState: boolean) => {
    try {
      await fetch(`/api/flags/${key}/toggle`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-Tenant-ID': '00000000-0000-0000-0000-000000000000'
        },
        body: JSON.stringify({ enabled: !currentState })
      })
      fetchFlags()
    } catch (e) {
      console.error(e)
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-semibold text-gray-900 mb-6">Feature Flags</h1>

      <div className="bg-white shadow rounded-lg overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Key</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Description</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {loading && <tr><td colSpan={4} className="p-4 text-center text-gray-500">Loading...</td></tr>}
            {!loading && flags.length === 0 && <tr><td colSpan={4} className="p-4 text-center text-gray-500">No flags found.</td></tr>}
            {flags.map(flag => (
              <tr key={flag.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{flag.key}</td>
                <td className="px-6 py-4 text-sm text-gray-500">{flag.description}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                    flag.defaultEnabled ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                  }`}>
                    {flag.defaultEnabled ? 'Enabled' : 'Disabled'}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <button
                    onClick={() => toggleFlag(flag.key, flag.defaultEnabled)}
                    className="text-blue-600 hover:text-blue-900"
                  >
                    Toggle
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
