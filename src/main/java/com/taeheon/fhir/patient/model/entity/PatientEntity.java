package com.taeheon.fhir.patient.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.r5.model.Enumerations.AdministrativeGender;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "patient")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientEntity {
    @Id
    @GeneratedValue
    private Long patientNo;

    private String name;
    private String phone;
    private String identifierSystem;
    private String identifierValue;

    @Enumerated(EnumType.STRING)
    private AdministrativeGender gender;
    private LocalDate birthDate;
    private String district;
    private String roadName;
    private String detailedAddress;
    private String complements;
    private String postalCode;

    private List<Identifier> getIdentifier() {
        Identifier identifier = new Identifier();
        Coding coding = new Coding();
        coding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");
        coding.setCode("MR");
        identifier.setType(new CodeableConcept(coding));
        identifier.setSystem(identifierSystem);
        identifier.setValue(identifierValue);
        return List.of(identifier);
    }

    private List<HumanName> getFhirName() {
        List<HumanName> nameList = new ArrayList<>();
        HumanName humanName = new HumanName();
        humanName.setText(name);
        nameList.add(humanName);
        return nameList;
    }

    private List<ContactPoint> getTelecom() {
        List<ContactPoint> telecomList = new ArrayList<>();
        ContactPoint contactPoint = new ContactPoint();
        contactPoint.setSystem(ContactPoint.ContactPointSystem.PHONE);
        contactPoint.setValue(phone);
        telecomList.add(contactPoint);
        return telecomList;
    }

    private Date getFhirBirthDate() {
        return Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Address getAddress() {
        Address address = new Address();
        address.setExtension(List.of(this.getAddressExtension()));
        address.setText(
                String.format(
                        "%s %s %s (%s)",
                        district,
                        roadName,
                        detailedAddress,
                        complements
                ));
        address.setPostalCode(this.postalCode);
        return address;
    }

    private Extension getAddressExtension() {
        Extension extension = new Extension("https://example.org/fhir/krcore/StructureDefinition/krcore-roadnameaddress");
        extension.addExtension("district", new StringType(district));
        extension.addExtension("roadName", new StringType(roadName));
        extension.addExtension("detailedAddress", new StringType(detailedAddress));
        extension.addExtension("complements", new StringType(complements));
        return extension;
    }

    public Patient toFhirModel() {
        Patient patient = new Patient();

        patient.setId(patientNo.toString());
        patient.setIdentifier(getIdentifier());
        patient.setGender(gender);
        patient.setName(this.getFhirName());
        patient.setTelecom(this.getTelecom());
        patient.setBirthDate(this.getFhirBirthDate());
        patient.setAddress(List.of(this.getAddress()));

        return patient;
    }
}