package com.taeheon.fhir.observation.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hl7.fhir.r5.model.Enumerations.ObservationStatus;
import org.hl7.fhir.r5.model.Observation;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "observation")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ObservationEntity {
    @Id
    @GeneratedValue
    private Long observationNo;

    private LocalDateTime effectiveDateTime;

    private String observationType;

    @Enumerated(EnumType.STRING)
    private ObservationStatus status;

    private Boolean isDeleted;

    private Long insertUserNo;

    @Setter
    private Long updateUserNo;

    @CreationTimestamp
    private LocalDateTime insertTimestamp;

    @Setter
    @UpdateTimestamp
    private LocalDateTime updateTimestamp;

    public abstract Observation toFhirModel();
}