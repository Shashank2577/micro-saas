import React from "react";

import { render, screen } from '@testing-library/react';
import { InsightCard } from '@/components/InsightCard';
import { describe, it, expect } from 'vitest';

describe('InsightCard', () => {
  it('renders insight information correctly', () => {
    const mockInsight = {
      id: '123',
      type: 'ANOMALY',
      title: 'Revenue Spike',
      description: 'Unusual spike in revenue detected.',
      impactScore: 0.8,
      status: 'NEW',
      createdAt: new Date().toISOString()
    };

    render(<InsightCard insight={mockInsight} />);
    
    expect(screen.getByText('Revenue Spike')).toBeInTheDocument();
    expect(screen.getByText('Unusual spike in revenue detected.')).toBeInTheDocument();
    expect(screen.getByText('ANOMALY')).toBeInTheDocument();
    expect(screen.getByText('NEW')).toBeInTheDocument();
  });
});
