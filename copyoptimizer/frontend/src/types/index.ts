export interface BaseEntity {
  id: string;
  tenantId: string;
  name: string;
  status: string;
  metadataJson: Record<string, any>;
  createdAt: string;
  updatedAt: string;
}

export interface CopyAsset extends BaseEntity {}
export interface Variant extends BaseEntity {}
export interface PredictionScore extends BaseEntity {}
export interface ExperimentPlan extends BaseEntity {}
export interface WinningVariant extends BaseEntity {}
export interface AudienceSegment extends BaseEntity {}
