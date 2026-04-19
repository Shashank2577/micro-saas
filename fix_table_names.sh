#!/bin/bash
sed -i 's/@Table(name = "LCap_LTables")/@Table(name = "cap_tables")/' equityintelligence/backend/src/main/java/com/microsaas/equityintelligence/model/CapTable.java
sed -i 's/@Table(name = "LShareholders")/@Table(name = "shareholders")/' equityintelligence/backend/src/main/java/com/microsaas/equityintelligence/model/Shareholder.java
sed -i 's/@Table(name = "LVesting_LSchedules")/@Table(name = "vesting_schedules")/' equityintelligence/backend/src/main/java/com/microsaas/equityintelligence/model/VestingSchedule.java
sed -i 's/@Table(name = "LFunding_LRounds")/@Table(name = "funding_rounds")/' equityintelligence/backend/src/main/java/com/microsaas/equityintelligence/model/FundingRound.java
sed -i 's/@Table(name = "LDilution_LScenarios")/@Table(name = "dilution_scenarios")/' equityintelligence/backend/src/main/java/com/microsaas/equityintelligence/model/DilutionScenario.java
sed -i 's/@Table(name = "LOption_LPool_LPlans")/@Table(name = "option_pool_plans")/' equityintelligence/backend/src/main/java/com/microsaas/equityintelligence/model/OptionPoolPlan.java
