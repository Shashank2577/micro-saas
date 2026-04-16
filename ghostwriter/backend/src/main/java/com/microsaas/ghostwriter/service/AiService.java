package com.microsaas.ghostwriter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;

    public String generateOutline(String topic, String voiceStyle) {
        String template = """
            You are an expert ghostwriter. Create a detailed outline for an article about: {topic}

            The tone and style should be: {voiceStyle}

            Return ONLY the outline, formatted with headings and bullet points. Do not include any introduction or conclusion remarks.
            """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of(
            "topic", topic,
            "voiceStyle", voiceStyle != null && !voiceStyle.isEmpty() ? voiceStyle : "Professional, clear, and engaging"
        ));

        return chatClient.prompt(prompt).call().content();
    }

    public String draftSection(String topic, String heading, String outlineContext, String voiceStyle) {
        String template = """
            You are an expert ghostwriter. You are writing an article about: {topic}

            Here is the full outline for context:
            {outlineContext}

            Please write the content for the following section:
            {heading}

            The tone and style MUST strictly follow these guidelines: {voiceStyle}

            Write at least 3-4 paragraphs. Return ONLY the content for this section, without the heading itself and without any conversational filler.
            """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of(
            "topic", topic,
            "heading", heading,
            "outlineContext", outlineContext,
            "voiceStyle", voiceStyle != null && !voiceStyle.isEmpty() ? voiceStyle : "Professional, clear, and engaging"
        ));

        return chatClient.prompt(prompt).call().content();
    }

    public String researchTopic(String topic) {
        String template = """
            You are an expert researcher. Please provide 3 key facts, statistics, or insights about the following topic: {topic}

            For each point, provide a brief excerpt and a plausible source or citation.

            Format as a JSON array of objects with keys: "excerpt", "citation", "sourceUrl".
            Ensure the output is ONLY valid JSON.
            """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("topic", topic));

        return chatClient.prompt(prompt).call().content();
    }

    public String extractVoiceStyle(String corpusContent) {
        String template = """
            Analyze the following text and extract its stylistic attributes, tone, vocabulary level, sentence structure, and typical rhetorical devices.

            Text:
            {content}

            Provide a concise summary (2-3 sentences) of the author's unique voice and style that a ghostwriter could use as instructions to replicate this voice.
            """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("content", corpusContent));

        return chatClient.prompt(prompt).call().content();
    }
}
