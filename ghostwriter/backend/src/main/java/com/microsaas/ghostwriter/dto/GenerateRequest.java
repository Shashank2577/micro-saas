package com.microsaas.ghostwriter.dto;

public class GenerateRequest {
    private String format;
    private String tone;
    private String topic;
    private String instructions;

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}
