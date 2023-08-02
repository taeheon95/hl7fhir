package com.taeheon.fhir.observation.model.entity;

import com.taeheon.fhir.patient.model.entity.PatientEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.r5.model.*;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "height")
public class Height extends ObservationEntity {
    private Double heightValue;
    @ManyToOne
    @JoinColumn(name = "patient_no")
    private PatientEntity patient;

    private List<CodeableConcept> category() {
        CodeableConcept codeableConcept = new CodeableConcept(
                new Coding(
                        "http://terminology.hl7.org/CodeSystem/observation-category",
                        "vital-signs",
                        "Vital Signs"
                )
        );
        return List.of(codeableConcept);
    }

    private CodeableConcept code() {
        return new CodeableConcept(
                new Coding(
                        "http://loinc.org",
                        "8302-2",
                        "Body height"
                )
        );
    }

    private Reference reference() {
        Reference reference = new Reference();
        reference.setId(patient.getPatientNo().toString());
        reference.setReference("Patient/" + patient.getPatientNo());
        return reference;
    }

    private DataType effective() {
        return new DateTimeType(
                Date.from(
                        this.getEffectiveDateTime()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                )
        );
    }

    private Quantity fhirValue() {
        Quantity quantity = new Quantity(this.heightValue);
        quantity.setCode("cm");
        quantity.setUnit("cm");
        quantity.setSystem("http://unitsofmeasure.org");
        return quantity;
    }

    @Override
    public Observation toFhirModel() {
        Observation observation = new Observation();
        observation.setId(this.getObservationNo().toString());
        observation.setStatus(this.getStatus());
        observation.setCategory(this.category());
        observation.setCode(this.code());
        observation.setSubject(this.reference());
        observation.setEffective(this.effective());
        observation.setValue(this.fhirValue());
        return observation;
    }
}