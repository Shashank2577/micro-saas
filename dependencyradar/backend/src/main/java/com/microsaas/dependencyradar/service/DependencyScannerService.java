package com.microsaas.dependencyradar.service;

import com.microsaas.dependencyradar.model.Dependency;
import com.microsaas.dependencyradar.repository.DependencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import jakarta.transaction.Transactional;

@Service
public class DependencyScannerService {

    private final DependencyRepository dependencyRepository;
    private final VulnerabilityScannerService vulnerabilityScanner;
    private final GithubClientService githubClientService;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public DependencyScannerService(DependencyRepository dependencyRepository, 
                                  VulnerabilityScannerService vulnerabilityScanner,
                                  GithubClientService githubClientService) {
        this.dependencyRepository = dependencyRepository;
        this.vulnerabilityScanner = vulnerabilityScanner;
        this.githubClientService = githubClientService;
    }

    @Transactional
    public void scanRepository(String owner, String repo, String branch, String repoId) {
        System.out.println("Scanning repository: " + owner + "/" + repo);
        
        List<Dependency> parsedDependencies = new ArrayList<>();
        
        String pomContent = githubClientService.fetchFileContent(owner, repo, branch, "pom.xml");
        if(pomContent != null) {
            parsedDependencies.addAll(parsePomXml(pomContent, repoId));
        }

        String packageJsonContent = githubClientService.fetchFileContent(owner, repo, branch, "package.json");
        if(packageJsonContent != null) {
            parsedDependencies.addAll(parsePackageJson(packageJsonContent, repoId));
        }
        
        // Delete previous dependencies for this repo to avoid duplicates
        List<Dependency> existing = dependencyRepository.findByRepoId(repoId);
        if(!existing.isEmpty()) {
            dependencyRepository.deleteAll(existing);
        }

        // Save dependencies and check for vulnerabilities
        for (Dependency dep : parsedDependencies) {
            dep.setVulnerabilities(vulnerabilityScanner.scanForVulnerabilities(dep));
            dependencyRepository.save(dep);
        }
    }

    private List<Dependency> parsePomXml(String content, String repoId) {
        List<Dependency> deps = new ArrayList<>();
        try {
            JsonNode root = xmlMapper.readTree(content);
            JsonNode dependenciesNode = root.path("dependencies").path("dependency");
            
            if(dependenciesNode.isArray()) {
                for (JsonNode depNode : dependenciesNode) {
                    processMavenDependency(depNode, repoId, deps);
                }
            } else if (!dependenciesNode.isMissingNode() && !dependenciesNode.isEmpty()) {
                processMavenDependency(dependenciesNode, repoId, deps);
            }
        } catch (Exception e) {
            System.err.println("Failed to parse pom.xml: " + e.getMessage());
        }
        return deps;
    }

    private void processMavenDependency(JsonNode depNode, String repoId, List<Dependency> deps) {
        String groupId = depNode.path("groupId").asText();
        String artifactId = depNode.path("artifactId").asText();
        String version = depNode.path("version").asText(null);
        
        if(groupId != null && artifactId != null) {
            Dependency dep = new Dependency();
            dep.setRepoId(repoId);
            dep.setName(groupId + ":" + artifactId);
            dep.setCurrentVersion(version != null ? version : "unknown");
            
            // Try to lookup actual latest version from Maven Central
            String latestVersion = lookupMavenLatestVersion(groupId, artifactId);
            dep.setLatestVersion(latestVersion != null ? latestVersion : dep.getCurrentVersion());
            
            if (latestVersion != null && !latestVersion.equals(dep.getCurrentVersion()) && !"unknown".equals(dep.getCurrentVersion())) {
                dep.setOutdated(true);
            } else {
                dep.setOutdated(false);
            }
            
            deps.add(dep);
        }
    }

    private String lookupMavenLatestVersion(String groupId, String artifactId) {
        try {
            String url = "https://search.maven.org/solrsearch/select?q=g:\"{groupId}\" AND a:\"{artifactId}\"&core=gav&rows=1&wt=json";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, groupId, artifactId);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode root = jsonMapper.readTree(response.getBody());
                JsonNode docs = root.path("response").path("docs");
                if (docs.isArray() && docs.size() > 0) {
                    return docs.get(0).path("v").asText();
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch maven version for " + groupId + ":" + artifactId + " - " + e.getMessage());
        }
        return null;
    }

    private List<Dependency> parsePackageJson(String content, String repoId) {
        List<Dependency> deps = new ArrayList<>();
        try {
            JsonNode root = jsonMapper.readTree(content);
            
            // Parse dependencies
            JsonNode dependenciesNode = root.path("dependencies");
            dependenciesNode.fields().forEachRemaining(entry -> {
                processNpmDependency(entry.getKey(), entry.getValue().asText(), repoId, deps);
            });
            
            // Parse devDependencies
            JsonNode devDependenciesNode = root.path("devDependencies");
            devDependenciesNode.fields().forEachRemaining(entry -> {
                processNpmDependency(entry.getKey(), entry.getValue().asText(), repoId, deps);
            });
            
        } catch (Exception e) {
            System.err.println("Failed to parse package.json: " + e.getMessage());
        }
        return deps;
    }

    private void processNpmDependency(String name, String version, String repoId, List<Dependency> deps) {
        Dependency dep = new Dependency();
        dep.setRepoId(repoId);
        dep.setName(name);
        dep.setCurrentVersion(version);
        
        // Try to lookup actual latest version from NPM registry
        String latestVersion = lookupNpmLatestVersion(name);
        dep.setLatestVersion(latestVersion != null ? latestVersion : dep.getCurrentVersion());
        
        String cleanVersion = version.replaceAll("[^0-9.]", "");
        if (latestVersion != null && !latestVersion.equals(cleanVersion)) {
            dep.setOutdated(true);
        } else {
            dep.setOutdated(false);
        }
        
        deps.add(dep);
    }

    private String lookupNpmLatestVersion(String name) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
            String url = String.format("https://registry.npmjs.org/%s/latest", encodedName);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode root = jsonMapper.readTree(response.getBody());
                return root.path("version").asText();
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch npm version for " + name + " - " + e.getMessage());
        }
        return null;
    }
}
