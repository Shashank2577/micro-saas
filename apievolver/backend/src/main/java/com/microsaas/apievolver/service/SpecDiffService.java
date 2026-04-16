package com.microsaas.apievolver.service;

import com.microsaas.apievolver.model.ApiChange;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Service
public class SpecDiffService {

    public ApiChange diff(String oldSpecContent, String newSpecContent, String oldVersion, String newVersion, Long newSpecId) {
        OpenAPI oldOpenAPI = new OpenAPIV3Parser().readContents(oldSpecContent, null, null).getOpenAPI();
        OpenAPI newOpenAPI = new OpenAPIV3Parser().readContents(newSpecContent, null, null).getOpenAPI();

        List<String> changes = new ArrayList<>();
        List<String> breakingChanges = new ArrayList<>();

        if (oldOpenAPI != null && oldOpenAPI.getPaths() != null && newOpenAPI != null && newOpenAPI.getPaths() != null) {
            // Check removed endpoints
            for (String path : oldOpenAPI.getPaths().keySet()) {
                if (!newOpenAPI.getPaths().containsKey(path)) {
                    String msg = "Removed endpoint: " + path;
                    changes.add(msg);
                    breakingChanges.add(msg); // Removing an endpoint is a breaking change
                } else {
                    // Check methods inside the path
                    Map<io.swagger.v3.oas.models.PathItem.HttpMethod, io.swagger.v3.oas.models.Operation> oldMethods = oldOpenAPI.getPaths().get(path).readOperationsMap();
                    Map<io.swagger.v3.oas.models.PathItem.HttpMethod, io.swagger.v3.oas.models.Operation> newMethods = newOpenAPI.getPaths().get(path).readOperationsMap();
                    
                    for (io.swagger.v3.oas.models.PathItem.HttpMethod method : oldMethods.keySet()) {
                        if (!newMethods.containsKey(method)) {
                            String msg = "Removed method " + method + " from endpoint: " + path;
                            changes.add(msg);
                            breakingChanges.add(msg);
                        }
                    }
                }
            }

            // Check added endpoints
            for (String path : newOpenAPI.getPaths().keySet()) {
                if (!oldOpenAPI.getPaths().containsKey(path)) {
                    changes.add("Added endpoint: " + path);
                }
            }
        }

        // Extremely simplified schemas check for breaking changes (removed required field, type changed)
        if (oldOpenAPI != null && oldOpenAPI.getComponents() != null && oldOpenAPI.getComponents().getSchemas() != null &&
            newOpenAPI != null && newOpenAPI.getComponents() != null && newOpenAPI.getComponents().getSchemas() != null) {
            
            Map<String, Schema> oldSchemas = oldOpenAPI.getComponents().getSchemas();
            Map<String, Schema> newSchemas = newOpenAPI.getComponents().getSchemas();

            for (Map.Entry<String, Schema> entry : oldSchemas.entrySet()) {
                String schemaName = entry.getKey();
                Schema oldSchema = entry.getValue();
                Schema newSchema = newSchemas.get(schemaName);

                if (newSchema == null) {
                    changes.add("Removed schema: " + schemaName);
                    // could be breaking if used in responses, keeping it simple
                } else {
                    // Check required properties removed
                    List<String> oldRequired = oldSchema.getRequired();
                    List<String> newRequired = newSchema.getRequired();
                    
                    if (oldRequired != null) {
                        for (String req : oldRequired) {
                            if (newRequired == null || !newRequired.contains(req)) {
                                // Wait, removing a required field from a request is NOT a breaking change.
                                // Removing a required field from a RESPONSE IS a breaking change.
                                // Let's simplify: if a property is removed entirely, and it was used, breaking.
                            }
                        }
                    }
                    
                    // Check property type changed or removed entirely
                    Map<String, Schema> oldProps = oldSchema.getProperties();
                    Map<String, Schema> newProps = newSchema.getProperties();
                    
                    if (oldProps != null) {
                        for (Map.Entry<String, Schema> propEntry : oldProps.entrySet()) {
                            String propName = propEntry.getKey();
                            Schema oldPropSchema = propEntry.getValue();
                            
                            if (newProps == null || !newProps.containsKey(propName)) {
                                String msg = "Removed property " + propName + " from schema " + schemaName;
                                changes.add(msg);
                                breakingChanges.add(msg); // Assuming removed property is a breaking change
                            } else {
                                Schema newPropSchema = newProps.get(propName);
                                if (oldPropSchema.getType() != null && !oldPropSchema.getType().equals(newPropSchema.getType())) {
                                    String msg = "Type changed for property " + propName + " in schema " + schemaName + " from " + oldPropSchema.getType() + " to " + newPropSchema.getType();
                                    changes.add(msg);
                                    breakingChanges.add(msg); // Type change is breaking
                                }
                            }
                        }
                    }
                    
                    // Check if new REQUIRED property was added
                    if (newRequired != null) {
                        for (String req : newRequired) {
                            if (oldRequired == null || !oldRequired.contains(req)) {
                                if (newProps != null && newProps.containsKey(req)) {
                                    // if it was newly added and required, it's breaking (for requests)
                                    if (oldProps == null || !oldProps.containsKey(req)) {
                                        String msg = "Added new required property " + req + " to schema " + schemaName;
                                        changes.add(msg);
                                        breakingChanges.add(msg);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        ApiChange apiChange = new ApiChange();
        apiChange.setSpecId(newSpecId);
        apiChange.setOldVersion(oldVersion);
        apiChange.setNewVersion(newVersion);
        apiChange.setChanges(changes);
        apiChange.setBreakingChanges(breakingChanges);

        return apiChange;
    }
}
