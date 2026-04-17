package com.crosscutting.starter.search;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@AutoConfiguration
public class SearchAutoConfiguration {

    @Bean
    public SearchService searchService(JdbcTemplate jdbcTemplate) {
        return new SearchService(jdbcTemplate);
    }

    @Bean
    public SearchController searchController(SearchService searchService) {
        return new SearchController(searchService);
    }
}
