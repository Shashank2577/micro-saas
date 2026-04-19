import React from 'react'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Mitigation Options',
}

export default function MitigationOptionsPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Mitigation Options</h1>
      <p>View and manage your mitigation options.</p>
    </div>
  )
}
