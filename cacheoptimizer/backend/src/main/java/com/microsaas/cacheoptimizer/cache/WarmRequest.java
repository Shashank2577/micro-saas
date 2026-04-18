package com.microsaas.cacheoptimizer.cache;

import java.util.List;

public class WarmRequest {
    private String namespace;
    private List<String> keys;

    public String getNamespace() { return namespace; }
    public void setNamespace(String namespace) { this.namespace = namespace; }
    public List<String> getKeys() { return keys; }
    public void setKeys(List<String> keys) { this.keys = keys; }
}
