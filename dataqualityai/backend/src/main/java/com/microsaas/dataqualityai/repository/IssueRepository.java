package com.microsaas.dataqualityai.repository;

import com.microsaas.dataqualityai.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, String> {
}
