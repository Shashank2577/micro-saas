import React from 'react'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Cash Positions',
}

export default function CashPositionsPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Cash Positions</h1>
      <p>View and manage your cash positions.</p>
    </div>
  )
}
