package com.microsaas.wealthplan.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.microsaas.wealthplan.entity.EstateDocument;
import com.microsaas.wealthplan.entity.Goal;
import com.microsaas.wealthplan.repository.EstateDocumentRepository;
import com.microsaas.wealthplan.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final GoalRepository goalRepository;
    private final EstateDocumentRepository estateRepository;

    public byte[] generatePlanPdf() {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Comprehensive Financial Plan"));
            document.add(new Paragraph("Tenant: " + TenantContext.require().toString()));

            List<Goal> goals = goalRepository.findByTenantId(TenantContext.require().toString());
            document.add(new Paragraph("Goals (" + goals.size() + "):"));
            for (Goal goal : goals) {
                document.add(new Paragraph("- " + goal.getName() + ": $" + goal.getTargetAmount()));
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public List<EstateDocument> getEstateChecklist() {
        return estateRepository.findByTenantId(TenantContext.require().toString());
    }

    public String exportGoalsCsv() {
        List<Goal> goals = goalRepository.findByTenantId(TenantContext.require().toString());
        StringBuilder sb = new StringBuilder();
        sb.append("id,name,type,targetAmount,currentAmount,targetDate\n");
        for (Goal g : goals) {
            sb.append(g.getId()).append(",")
              .append(g.getName()).append(",")
              .append(g.getType()).append(",")
              .append(g.getTargetAmount()).append(",")
              .append(g.getCurrentAmount()).append(",")
              .append(g.getTargetDate()).append("\n");
        }
        return sb.toString();
    }
}
