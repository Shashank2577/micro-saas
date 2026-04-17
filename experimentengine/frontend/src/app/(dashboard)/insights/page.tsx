'use client'
import { useState } from 'react'

export default function InsightsPage() {
  const [productArea, setProductArea] = useState('')
  const [metricId, setMetricId] = useState('')
  const [suggestions, setSuggestions] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSuggest = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    try {
      const res = await fetch('/api/ai/suggest-hypotheses', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productArea, metricId })
      })
      const data = await res.json()
      setSuggestions(data.hypotheses || 'No suggestions available')
    } catch (err) {
      console.error(err)
      setSuggestions('Error fetching suggestions')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-2xl font-semibold text-gray-900 mb-6">AI Insights & Hypothesis Gen</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="md:col-span-1 bg-white p-6 shadow rounded-lg h-fit">
          <form onSubmit={handleSuggest} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Product Area</label>
              <input
                type="text"
                value={productArea}
                onChange={e => setProductArea(e.target.value)}
                placeholder="e.g. Checkout flow"
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Metric</label>
              <input
                type="text"
                value={metricId}
                onChange={e => setMetricId(e.target.value)}
                placeholder="e.g. Conversion Rate"
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border"
                required
              />
            </div>
            <button
              type="submit"
              disabled={loading}
              className="w-full px-4 py-2 bg-purple-600 text-white rounded-md hover:bg-purple-700 disabled:bg-purple-300"
            >
              {loading ? 'Generating...' : 'Suggest Hypotheses'}
            </button>
          </form>
        </div>

        <div className="md:col-span-2 bg-white p-6 shadow rounded-lg min-h-[300px]">
          <h2 className="text-lg font-medium mb-4">AI Suggestions</h2>
          {suggestions ? (
            <div className="prose text-sm whitespace-pre-wrap">{suggestions}</div>
          ) : (
            <div className="text-gray-500 text-center mt-20">Fill out the form to generate AI hypotheses.</div>
          )}
        </div>
      </div>
    </div>
  )
}
