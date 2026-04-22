package com.microsaas.ecosystemmap.service;

import com.microsaas.ecosystemmap.entity.Connection;
import com.microsaas.ecosystemmap.entity.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisualizationService {

    private final NodeService nodeService;
    private final ConnectionService connectionService;

    public Map<String, Object> getEcosystemGraph(UUID ecosystemId) {
        List<Node> nodes = nodeService.getNodesByEcosystem(ecosystemId);
        List<Connection> connections = connectionService.getConnectionsByEcosystem(ecosystemId);

        List<Map<String, Object>> nodeData = nodes.stream()
                .map(n -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", n.getId().toString());
                    map.put("label", n.getAppName());
                    map.put("type", n.getNodeType());
                    map.put("status", n.getStatus());
                    return map;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> edgeData = connections.stream()
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", c.getId().toString());
                    map.put("source", c.getSourceNode().getId().toString());
                    map.put("target", c.getTargetNode().getId().toString());
                    map.put("label", c.getConnectionType());
                    map.put("status", c.getStatus());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> graph = new HashMap<>();
        graph.put("nodes", nodeData);
        graph.put("edges", edgeData);

        return graph;
    }
}
