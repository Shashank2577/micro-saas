ALTER TABLE user_profiles ADD COLUMN inheritance_goal DECIMAL(19, 2) DEFAULT 0.0;
ALTER TABLE user_profiles ADD COLUMN wants_annuity BOOLEAN DEFAULT FALSE;

ALTER TABLE projections ADD COLUMN roth_conversion_amount DECIMAL(19, 2) DEFAULT 0.0;
ALTER TABLE projections ADD COLUMN rmd_amount DECIMAL(19, 2) DEFAULT 0.0;
ALTER TABLE projections ADD COLUMN stress_test_survival_rate DECIMAL(5, 2) DEFAULT 0.0;
ALTER TABLE projections ADD COLUMN annuity_guaranteed_income DECIMAL(19, 2) DEFAULT 0.0;
ALTER TABLE projections ADD COLUMN tax_strategy_order VARCHAR(255);
