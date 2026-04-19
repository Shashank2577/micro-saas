import React from 'react'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Scenario Runs',
}

export default function ScenarioRunsPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Scenario Runs</h1>
      <p>View and manage your scenario runs.</p>
    </div>
  )
}
