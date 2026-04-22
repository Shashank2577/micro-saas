package com.microsaas.brandvoice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "tone_of_voices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToneOfVoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    private UUID brandProfileId;
    private String adjective;
    private String definition;
    private String usageExample;
}
