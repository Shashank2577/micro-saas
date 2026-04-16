package com.microsaas.videonarrator.service;

import com.microsaas.videonarrator.model.*;
import com.microsaas.videonarrator.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoNarratorService {

    private final VideoAssetRepository videoAssetRepository;
    private final TranscriptRepository transcriptRepository;
    private final VideoChapterRepository videoChapterRepository;
    private final DerivedContentRepository derivedContentRepository;
    private final VideoClipRepository videoClipRepository;

    @Transactional
    public VideoAsset registerVideo(String title, String fileUrl, Integer durationSeconds, UUID tenantId) {
        VideoAsset asset = VideoAsset.builder()
                .tenantId(tenantId)
                .title(title)
                .fileUrl(fileUrl)
                .durationSeconds(durationSeconds)
                .status(VideoStatus.UPLOADED)
                .createdAt(LocalDateTime.now())
                .build();
        return videoAssetRepository.save(asset);
    }

    public List<VideoAsset> getVideos(UUID tenantId) {
        return videoAssetRepository.findByTenantId(tenantId);
    }

    public Optional<VideoAsset> getVideo(UUID id, UUID tenantId) {
        return videoAssetRepository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public Transcript generateTranscript(UUID videoId, UUID tenantId) {
        VideoAsset asset = videoAssetRepository.findByIdAndTenantId(videoId, tenantId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        asset.setStatus(VideoStatus.PROCESSING);
        videoAssetRepository.save(asset);

        // Simulate AI Transcript generation
        Transcript transcript = Transcript.builder()
                .tenantId(tenantId)
                .videoId(videoId)
                .fullText("This is a simulated transcript for video " + asset.getTitle())
                .speakers(List.of(new Speaker("1", "Speaker 1")))
                .segments(List.of(new Segment(0, asset.getDurationSeconds() != null ? asset.getDurationSeconds() * 1000 : 10000, "Speaker 1", "Hello world.")))
                .generatedAt(LocalDateTime.now())
                .build();

        transcript = transcriptRepository.save(transcript);

        asset.setStatus(VideoStatus.DONE);
        videoAssetRepository.save(asset);

        return transcript;
    }

    public Optional<Transcript> getTranscript(UUID videoId, UUID tenantId) {
        return transcriptRepository.findByVideoIdAndTenantId(videoId, tenantId);
    }

    @Transactional
    public List<VideoChapter> generateChapters(UUID videoId, UUID tenantId) {
        videoChapterRepository.deleteByVideoIdAndTenantId(videoId, tenantId);

        VideoChapter chapter1 = VideoChapter.builder()
                .tenantId(tenantId)
                .videoId(videoId)
                .title("Introduction")
                .startMs(0)
                .endMs(60000)
                .summary("Brief intro")
                .chapterOrder(1)
                .build();

        VideoChapter chapter2 = VideoChapter.builder()
                .tenantId(tenantId)
                .videoId(videoId)
                .title("Main Content")
                .startMs(60000)
                .endMs(120000)
                .summary("Core discussion")
                .chapterOrder(2)
                .build();

        return videoChapterRepository.saveAll(List.of(chapter1, chapter2));
    }

    @Transactional
    public List<DerivedContent> generateDerivedContent(UUID videoId, UUID tenantId) {
        derivedContentRepository.deleteByVideoIdAndTenantId(videoId, tenantId);

        DerivedContent blog = DerivedContent.builder()
                .tenantId(tenantId)
                .videoId(videoId)
                .type(ContentType.BLOG_POST)
                .content("Simulated blog post content...")
                .generatedAt(LocalDateTime.now())
                .build();

        DerivedContent social = DerivedContent.builder()
                .tenantId(tenantId)
                .videoId(videoId)
                .type(ContentType.SOCIAL_CLIPS)
                .content("Tweet 1: Check out this video! \n Tweet 2: Amazing insights!")
                .generatedAt(LocalDateTime.now())
                .build();

        return derivedContentRepository.saveAll(List.of(blog, social));
    }

    @Transactional
    public List<VideoClip> generateClips(UUID videoId, UUID tenantId) {
        videoClipRepository.deleteByVideoIdAndTenantId(videoId, tenantId);

        VideoClip clip1 = VideoClip.builder()
                .tenantId(tenantId)
                .videoId(videoId)
                .startMs(30000)
                .endMs(45000)
                .engagementScore(new BigDecimal("9.5"))
                .clipUrl("https://example.com/clip1.mp4")
                .reason("High energy segment")
                .build();

        return videoClipRepository.saveAll(List.of(clip1));
    }

    public List<VideoClip> getClips(UUID videoId, UUID tenantId) {
        return videoClipRepository.findByVideoIdAndTenantIdOrderByEngagementScoreDesc(videoId, tenantId);
    }
}
