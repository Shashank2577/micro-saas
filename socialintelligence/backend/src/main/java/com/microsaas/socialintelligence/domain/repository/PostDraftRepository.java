package com.microsaas.socialintelligence.domain.repository;

import com.microsaas.socialintelligence.domain.model.PostDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostDraftRepository extends JpaRepository<PostDraft, UUID> {
}
