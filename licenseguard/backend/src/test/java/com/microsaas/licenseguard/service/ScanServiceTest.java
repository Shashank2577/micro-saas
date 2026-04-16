package com.microsaas.licenseguard.service;

import com.microsaas.licenseguard.domain.Dependency;
import com.microsaas.licenseguard.domain.LicenseViolation;
import com.microsaas.licenseguard.domain.Repository;
import com.microsaas.licenseguard.repository.DependencyRepository;
import com.microsaas.licenseguard.repository.LicenseViolationRepository;
import com.microsaas.licenseguard.repository.RepositoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScanServiceTest {

    @Mock
    private RepositoryRepository repositoryRepository;

    @Mock
    private DependencyRepository dependencyRepository;

    @Mock
    private LicenseViolationRepository violationRepository;

    @InjectMocks
    private ScanService scanService;

    @Captor
    private ArgumentCaptor<Repository> repositoryCaptor;

    @Captor
    private ArgumentCaptor<List<Dependency>> dependenciesCaptor;

    @Captor
    private ArgumentCaptor<LicenseViolation> violationCaptor;

    private UUID tenantId;
    private UUID repositoryId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        repositoryId = UUID.randomUUID();
    }

    @Test
    void registerRepo_savesRepository() {
        String name = "my-repo";
        String url = "https://github.com/my/repo";

        when(repositoryRepository.save(any(Repository.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Repository result = scanService.registerRepo(name, url, tenantId);

        verify(repositoryRepository).save(repositoryCaptor.capture());
        Repository saved = repositoryCaptor.getValue();
        
        assertThat(saved.getName()).isEqualTo(name);
        assertThat(saved.getRepoUrl()).isEqualTo(url);
        assertThat(saved.getTenantId()).isEqualTo(tenantId);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void scanRepo_createsDependencies() {
        Repository repo = Repository.builder()
                .id(repositoryId)
                .tenantId(tenantId)
                .build();
                
        when(repositoryRepository.findById(repositoryId)).thenReturn(Optional.of(repo));

        scanService.scanRepo(repositoryId, tenantId);

        verify(dependencyRepository).saveAll(dependenciesCaptor.capture());
        List<Dependency> savedDeps = dependenciesCaptor.getValue();
        
        assertThat(savedDeps).hasSize(5);
        assertThat(savedDeps).extracting(Dependency::getName)
                .containsExactlyInAnyOrder("spring-boot", "lombok", "jackson", "commons-lang3", "gpl-lib");
    }

    @Test
    void detectViolations_flagsGplAsCopyleftRisk() {
        Dependency gplDep = Dependency.builder()
                .id(UUID.randomUUID())
                .repositoryId(repositoryId)
                .tenantId(tenantId)
                .name("gpl-lib")
                .license("GPL_3")
                .build();
                
        when(dependencyRepository.findByRepositoryIdAndTenantId(repositoryId, tenantId))
                .thenReturn(List.of(gplDep));

        scanService.detectViolations(repositoryId, tenantId);

        verify(violationRepository).save(violationCaptor.capture());
        LicenseViolation violation = violationCaptor.getValue();
        
        assertThat(violation.getViolationType()).isEqualTo("COPYLEFT_RISK");
        assertThat(violation.getSeverity()).isEqualTo("HIGH");
        assertThat(violation.getDependencyId()).isEqualTo(gplDep.getId());
    }
}
