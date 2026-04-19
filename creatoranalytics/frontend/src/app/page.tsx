import Link from 'next/link'

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center p-24">
      <h1 className="text-4xl font-bold mb-8">Creator Analytics</h1>
      <div className="grid grid-cols-2 gap-4">
        <Link href="/creator-analytics/content-assets" className="p-4 border rounded hover:bg-gray-100 dark:hover:bg-gray-800">
          Content Assets
        </Link>
        <Link href="/creator-analytics/channel-metrics" className="p-4 border rounded hover:bg-gray-100 dark:hover:bg-gray-800">
          Channel Metrics
        </Link>
        <Link href="/creator-analytics/attribution-models" className="p-4 border rounded hover:bg-gray-100 dark:hover:bg-gray-800">
          Attribution Models
        </Link>
        <Link href="/creator-analytics/roisnapshots" className="p-4 border rounded hover:bg-gray-100 dark:hover:bg-gray-800">
          ROI Snapshots
        </Link>
        <Link href="/creator-analytics/audience-segments" className="p-4 border rounded hover:bg-gray-100 dark:hover:bg-gray-800">
          Audience Segments
        </Link>
        <Link href="/creator-analytics/performance-insights" className="p-4 border rounded hover:bg-gray-100 dark:hover:bg-gray-800">
          Performance Insights
        </Link>
      </div>
    </main>
  )
}
