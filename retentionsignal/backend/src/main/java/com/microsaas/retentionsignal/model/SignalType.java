package com.microsaas.retentionsignal.model;

public enum SignalType {
    LOW_ENGAGEMENT(25),
    COMP_GAP(30),
    SLOW_PROGRESSION(20),
    MANAGER_ISSUES(15),
    PEER_DEPARTURES(10);

    private final int weight;

    SignalType(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
