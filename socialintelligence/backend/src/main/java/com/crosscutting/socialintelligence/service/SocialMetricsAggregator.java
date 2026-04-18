package com.crosscutting.socialintelligence.service;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;





import com.crosscutting.socialintelligence.domain.EngagementMetric;
import com.crosscutting.socialintelligence.domain.PlatformAccount;
import com.crosscutting.socialintelligence.repository.EngagementMetricRepository;
import com.crosscutting.socialintelligence.repository.PlatformAccountRepository;
import com.crosscutting.socialintelligence.dto.UnifiedMetrics;
import com.crosscutting.socialintelligence.dto.EngagementTrends;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SocialMetricsAggregator {

    private final EngagementMetricRepository metricRepository;
    private final PlatformAccountRepository accountRepository;

    public SocialMetricsAggregator(EngagementMetricRepository metricRepository, PlatformAccountRepository accountRepository) {
        this.metricRepository = metricRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void syncMetrics(String tenantId) {
        List<PlatformAccount> accounts = accountRepository.findByTenantId(tenantId);
        for (PlatformAccount account : accounts) {
            EngagementMetric metric = EngagementMetric.builder()
                    .tenantId(tenantId)
                    .account(account)
                    .metricDate(LocalDate.now())
                    .followersCount((long) (Math.random() * 10000))
                    .likesCount((long) (Math.random() * 500))
                    .commentsCount((long) (Math.random() * 100))
                    .viewsCount((long) (Math.random() * 5000))
                    .engagementRate(BigDecimal.valueOf(Math.random() * 10).setScale(2, RoundingMode.HALF_UP))
                    .build();
            metricRepository.save(metric);
        }
    }

    public UnifiedMetrics getUnifiedMetrics(String tenantId, LocalDate start, LocalDate end) {
        List<EngagementMetric> metrics = metricRepository.findByTenantIdAndMetricDateBetween(tenantId, start, end);
        if (metrics.isEmpty()) {
            return UnifiedMetrics.builder()
                    .totalFollowers(0L).totalLikes(0L).totalComments(0L).totalShares(0L).totalViews(0L)
                    .averageEngagementRate(BigDecimal.ZERO).build();
        }

        long followers = metrics.stream().mapToLong(EngagementMetric::getFollowersCount).sum();
        long likes = metrics.stream().mapToLong(EngagementMetric::getLikesCount).sum();
        long comments = metrics.stream().mapToLong(EngagementMetric::getCommentsCount).sum();
        long shares = metrics.stream().mapToLong(EngagementMetric::getSharesCount).sum();
        long views = metrics.stream().mapToLong(EngagementMetric::getViewsCount).sum();
        double avgRate = metrics.stream().mapToDouble(m -> m.getEngagementRate() != null ? m.getEngagementRate().doubleValue() : 0).average().orElse(0.0);

        return UnifiedMetrics.builder()
                .totalFollowers(followers)
                .totalLikes(likes)
                .totalComments(comments)
                .totalShares(shares)
                .totalViews(views)
                .averageEngagementRate(BigDecimal.valueOf(avgRate).setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    public EngagementTrends getEngagementTrends(String tenantId, int days) {
        LocalDate start = LocalDate.now().minusDays(days);
        LocalDate end = LocalDate.now();
        List<EngagementMetric> metrics = metricRepository.findByTenantIdAndMetricDateBetween(tenantId, start, end);

        Map<LocalDate, Double> avgByDate = metrics.stream()
                .collect(Collectors.groupingBy(
                        EngagementMetric::getMetricDate,
                        Collectors.averagingDouble(m -> m.getEngagementRate() != null ? m.getEngagementRate().doubleValue() : 0.0)
                ));

        List<EngagementTrends.DailyTrend> trends = avgByDate.entrySet().stream()
                .map(e -> new EngagementTrends.DailyTrend(e.getKey(), BigDecimal.valueOf(e.getValue()).setScale(2, RoundingMode.HALF_UP)))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());

        return EngagementTrends.builder().period(days + " days").trends(trends).build();
    }
}
