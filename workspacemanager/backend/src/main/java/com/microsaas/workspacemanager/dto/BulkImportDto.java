package com.microsaas.workspacemanager.dto;

import java.util.List;

public class BulkImportDto {
    private List<MemberImportRow> rows;

    public static class MemberImportRow {
        private String email;
        private String name;
        private String role;
    
        public String getEmail() { return this.email; }
        public void setEmail(String email) { this.email = email; }

        public String getName() { return this.name; }
        public void setName(String name) { this.name = name; }

        public String getRole() { return this.role; }
        public void setRole(String role) { this.role = role; }
    }

    public List<MemberImportRow> getRows() { return this.rows; }
    public void setRows(List<MemberImportRow> rows) { this.rows = rows; }
}
