package com.microsaas.hiresignal.service;

import com.microsaas.hiresignal.model.JobRequisition;
import com.microsaas.hiresignal.repository.JobRequisitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequisitionService {

    private final JobRequisitionRepository repository;

    @Transactional
    public JobRequisition createReq(String title, String department, String requirements) {
        JobRequisition req = JobRequisition.builder()
                .title(title)
                .department(department)
                .requirements(requirements)
                .status(JobRequisition.Status.OPEN)
                .openedAt(ZonedDateTime.now())
                .build();
        return repository.save(req);
    }

    @Transactional
    public JobRequisition closeReq(UUID id) {
        JobRequisition req = repository.findById(id).orElseThrow();
        req.setStatus(JobRequisition.Status.CLOSED);
        return repository.save(req);
    }

    public List<JobRequisition> listReqs() {
        return repository.findAll();
    }
}
