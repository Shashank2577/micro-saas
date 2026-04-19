import React from 'react'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Funding Events',
}

export default function FundingEventsPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Funding Events</h1>
      <p>View and manage your funding events.</p>
    </div>
  )
}
