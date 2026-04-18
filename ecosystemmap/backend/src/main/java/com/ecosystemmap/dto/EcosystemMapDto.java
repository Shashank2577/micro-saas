package com.ecosystemmap.dto;

import com.ecosystemmap.domain.DataFlow;
import com.ecosystemmap.domain.DeployedApp;

import java.util.List;


public class EcosystemMapDto {
    private List<DeployedApp> apps;
    private List<DataFlow> flows;

    public List<DeployedApp> getApps() {
        return apps;
    }

    public void setApps(List<DeployedApp> apps) {
        this.apps = apps;
    }

    public List<DataFlow> getFlows() {
        return flows;
    }

    public void setFlows(List<DataFlow> flows) {
        this.flows = flows;
    }
}
