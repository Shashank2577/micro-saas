package com.microsaas.creatoranalytics.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Map;

@Entity
@Table(name = "attribution_models")
@Getter
@Setter
public class AttributionModel extends BaseEntity {

    @Column(nullable = false, length = 180)
    private String name;

    @Column(nullable = false, length = 40)
    private String status;

    @Type(JsonType.class)
    @Column(name = "metadata_json", columnDefinition = "jsonb")
    private Map<String, Object> metadata;
}
