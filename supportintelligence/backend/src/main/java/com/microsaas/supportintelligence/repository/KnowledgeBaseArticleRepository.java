package com.microsaas.supportintelligence.repository;

import com.microsaas.supportintelligence.entity.KnowledgeBaseArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KnowledgeBaseArticleRepository extends JpaRepository<KnowledgeBaseArticle, UUID> {
}
