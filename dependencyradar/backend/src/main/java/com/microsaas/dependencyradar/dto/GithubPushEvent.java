package com.microsaas.dependencyradar.dto;

import lombok.Data;
import java.util.List;

@Data
public class GithubPushEvent {
    private String ref;
    private Repository repository;
    private List<Commit> commits;

    @Data
    public static class Repository {
        private String id;
        private String name;
        private String full_name;
        private String url;
        private Owner owner;
    }

    @Data
    public static class Owner {
        private String name;
        private String login;
    }

    @Data
    public static class Commit {
        private String id;
        private String message;
        private List<String> added;
        private List<String> modified;
        private List<String> removed;
    }
}
