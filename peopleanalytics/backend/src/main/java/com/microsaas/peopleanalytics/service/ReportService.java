package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final EmployeeService employeeService;

    public byte[] exportEmployeesToPdf() {
        return "PDF Content Placeholder".getBytes();
    }

    public byte[] exportEmployeesToCsv() {
        return "CSV Content Placeholder".getBytes();
    }
}
