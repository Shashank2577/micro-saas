package com.microsaas.ghostwriter.service;

import com.microsaas.ghostwriter.model.CorpusItem;
import com.microsaas.ghostwriter.model.ModelStatus;
import com.microsaas.ghostwriter.model.VoiceModel;
import com.microsaas.ghostwriter.repository.CorpusItemRepository;
import com.microsaas.ghostwriter.repository.VoiceModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoiceModelService {

    private final VoiceModelRepository voiceModelRepository;
    private final CorpusItemRepository corpusItemRepository;
    private final AiService aiService;

    @Transactional
    public VoiceModel createVoiceModel(String name, UUID ownerId, UUID tenantId) {
        VoiceModel model = VoiceModel.builder()
                .name(name)
                .ownerId(ownerId)
                .tenantId(tenantId)
                .status(ModelStatus.READY)
                .corpusSize(0)
                .build();
        return voiceModelRepository.save(model);
    }

    @Transactional
    public CorpusItem addCorpusContent(UUID modelId, String title, String content, UUID tenantId) {
        VoiceModel model = voiceModelRepository.findByIdAndTenantId(modelId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Voice model not found"));

        CorpusItem item = CorpusItem.builder()
                .voiceModelId(modelId)
                .title(title)
                .content(content)
                .tenantId(tenantId)
                .build();

        CorpusItem savedItem = corpusItemRepository.save(item);

        model.setCorpusSize(model.getCorpusSize() + 1);
        model.setStatus(ModelStatus.UPDATING);
        voiceModelRepository.save(model);

        return savedItem;
    }

    @Transactional
    public VoiceModel trainModel(UUID modelId, UUID tenantId) {
        VoiceModel model = voiceModelRepository.findByIdAndTenantId(modelId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Voice model not found"));

        model.setStatus(ModelStatus.TRAINING);
        voiceModelRepository.save(model);

        // Simulate training by extracting voice style from corpus
        List<CorpusItem> items = corpusItemRepository.findByVoiceModelIdAndTenantId(modelId, tenantId);

        StringBuilder corpusContent = new StringBuilder();
        for (CorpusItem item : items) {
            corpusContent.append(item.getContent()).append("\n\n");
        }

        if (!corpusContent.isEmpty()) {
            String styleExtraction = aiService.extractVoiceStyle(corpusContent.toString());
            model.setStyleAttributes(Map.of("extracted_style", styleExtraction));
        } else {
            model.setStyleAttributes(Map.of("extracted_style", "Standard professional voice"));
        }

        model.setStatus(ModelStatus.READY);
        model.setTrainedAt(Instant.now());

        return voiceModelRepository.save(model);
    }
}
