package com.microsaas.peopleanalytics.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.microsaas.peopleanalytics.model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final EmployeeService employeeService;

    public byte[] exportEmployeesToPdf() {
        List<Employee> employees = employeeService.getAllEmployees();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Employee Report").setBold().setFontSize(18));
            for (Employee emp : employees) {
                document.add(new Paragraph(String.format("ID: %s | Dept: %s | Role: %s",
                    emp.getId(), emp.getDepartment(), emp.getRole())));
            }
        } catch (Exception e) {
            log.error("Failed to generate PDF report", e);
        }
        return baos.toByteArray();
    }

    public byte[] exportEmployeesToCsv() {
        List<Employee> employees = employeeService.getAllEmployees();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PrintWriter writer = new PrintWriter(baos);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("ID", "Department", "Role", "Status"))) {

            for (Employee emp : employees) {
                csvPrinter.printRecord(emp.getId(), emp.getDepartment(), emp.getRole(), emp.getStatus());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            log.error("Failed to generate CSV report", e);
        }
        return baos.toByteArray();
    }
}
