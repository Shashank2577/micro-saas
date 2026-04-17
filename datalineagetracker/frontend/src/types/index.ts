export interface DataAsset {
    id: string;
    name: string;
    type: string;
    sourceSystem: string;
    classification: string;
    description: string;
    createdAt: string;
}

export interface LineageLink {
    id: string;
    sourceAsset: DataAsset;
    targetAsset: DataAsset;
    transformationLogic: string;
}

export interface GovernancePolicy {
    id: string;
    name: string;
    description: string;
    policyType: string;
    isActive: boolean;
}

export interface AuditLog {
    id: string;
    asset: DataAsset;
    action: string;
    status: string;
    reason: string;
    createdAt: string;
}
