import React from 'react'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Liquidity Forecasts',
}

export default function LiquidityForecastsPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Liquidity Forecasts</h1>
      <p>View and manage your liquidity forecasts.</p>
    </div>
  )
}
