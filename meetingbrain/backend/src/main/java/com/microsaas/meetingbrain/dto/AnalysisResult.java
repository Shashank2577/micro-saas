package com.microsaas.meetingbrain.dto;

import java.util.List;

public class AnalysisResult {
    private String summary;
    private List<DecisionData> decisions;
    private List<ActionItemData> actionItems;
    private List<QuestionData> openQuestions;

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public List<DecisionData> getDecisions() { return decisions; }
    public void setDecisions(List<DecisionData> decisions) { this.decisions = decisions; }
    public List<ActionItemData> getActionItems() { return actionItems; }
    public void setActionItems(List<ActionItemData> actionItems) { this.actionItems = actionItems; }
    public List<QuestionData> getOpenQuestions() { return openQuestions; }
    public void setOpenQuestions(List<QuestionData> openQuestions) { this.openQuestions = openQuestions; }

    public static class DecisionData {
        private String topic;
        private String description;
        private String decisionText;

        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDecisionText() { return decisionText; }
        public void setDecisionText(String decisionText) { this.decisionText = decisionText; }
    }

    public static class ActionItemData {
        private String description;
        private String owner;
        private String dueDate;

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getOwner() { return owner; }
        public void setOwner(String owner) { this.owner = owner; }
        public String getDueDate() { return dueDate; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    }

    public static class QuestionData {
        private String questionText;

        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }
    }
}
