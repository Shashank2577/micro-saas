import React from 'react'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Shortfall Alerts',
}

export default function ShortfallAlertsPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Shortfall Alerts</h1>
      <p>View and manage your shortfall alerts.</p>
    </div>
  )
}
