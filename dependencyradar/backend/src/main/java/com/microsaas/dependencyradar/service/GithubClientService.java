package com.microsaas.dependencyradar.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class GithubClientService {
    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchFileContent(String owner, String repo, String branch, String path) {
        String url = String.format("https://raw.githubusercontent.com/%s/%s/%s/%s", owner, repo, branch, path);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Failed to fetch file from github: " + url + " - " + e.getMessage());
        }
        return null;
    }
}
