export const API_BASE = '/api/v1/cashflow';

export interface CashflowPeriod {
    id: string;
    tenantId: string;
    name: string;
    status: string;
    metadataJson?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface CashMovement {
    id: string;
    tenantId: string;
    name: string;
    status: string;
    metadataJson?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface TrendSignal {
    id: string;
    tenantId: string;
    name: string;
    status: string;
    metadataJson?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface ForecastRun {
    id: string;
    tenantId: string;
    name: string;
    status: string;
    metadataJson?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface AnomalyFlag {
    id: string;
    tenantId: string;
    name: string;
    status: string;
    metadataJson?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface NarrativeInsight {
    id: string;
    tenantId: string;
    name: string;
    status: string;
    metadataJson?: string;
    createdAt?: string;
    updatedAt?: string;
}

export const fetchPeriods = async (): Promise<CashflowPeriod[]> => {
    const res = await fetch(`${API_BASE}/cashflow-periods`);
    if (!res.ok) throw new Error('Failed to fetch periods');
    return res.json();
};

export const fetchMovements = async (): Promise<CashMovement[]> => {
    const res = await fetch(`${API_BASE}/cash-movements`);
    if (!res.ok) throw new Error('Failed to fetch movements');
    return res.json();
};

export const fetchForecasts = async (): Promise<ForecastRun[]> => {
    const res = await fetch(`${API_BASE}/forecast-runs`);
    if (!res.ok) throw new Error('Failed to fetch forecasts');
    return res.json();
};

export const fetchAnomalies = async (): Promise<AnomalyFlag[]> => {
    const res = await fetch(`${API_BASE}/anomaly-flags`);
    if (!res.ok) throw new Error('Failed to fetch anomalies');
    return res.json();
};

export const fetchInsights = async (): Promise<TrendSignal[]> => {
    const res = await fetch(`${API_BASE}/trend-signals`);
    if (!res.ok) throw new Error('Failed to fetch insights');
    return res.json();
};

// Assuming NarrativeInsights map to Reporting Service domain
export const fetchReporting = async (): Promise<NarrativeInsight[]> => {
    const res = await fetch(`${API_BASE}/narrative-insights`);
    if (!res.ok) throw new Error('Failed to fetch reporting data');
    return res.json();
};
