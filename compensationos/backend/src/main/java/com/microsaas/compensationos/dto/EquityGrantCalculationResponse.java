package com.microsaas.compensationos.dto;

import java.math.BigDecimal;
import java.util.List;

public class EquityGrantCalculationResponse {
    private Long totalShares;
    private BigDecimal totalValue;
    private List<VestingEvent> vestingSchedule;

    public Long getTotalShares() { return this.totalShares; }
    public void setTotalShares(Long totalShares) { this.totalShares = totalShares; }
    public BigDecimal getTotalValue() { return this.totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
    public List<VestingEvent> getVestingSchedule() { return this.vestingSchedule; }
    public void setVestingSchedule(List<VestingEvent> vestingSchedule) { this.vestingSchedule = vestingSchedule; }

    public static class VestingEvent {
        private String date;
        private Long sharesVesting;
        private BigDecimal valueVesting;

        public String getDate() { return this.date; }
        public void setDate(String date) { this.date = date; }
        public Long getSharesVesting() { return this.sharesVesting; }
        public void setSharesVesting(Long sharesVesting) { this.sharesVesting = sharesVesting; }
        public BigDecimal getValueVesting() { return this.valueVesting; }
        public void setValueVesting(BigDecimal valueVesting) { this.valueVesting = valueVesting; }
    }
}
