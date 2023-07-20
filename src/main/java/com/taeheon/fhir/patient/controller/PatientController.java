package com.taeheon.fhir.patient.controller;

import ca.uhn.fhir.parser.IParser;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.r5.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hl7fhir/patient")
public class PatientController {

    private final IParser parser;

    @GetMapping(value ="/{id}", produces = {"application/fhir+json"})
    public ResponseEntity<?> getResult() {

        Patient patient = new Patient();
        patient.setId("1");

        Meta meta = new Meta();
        List<CanonicalType> profileList = new ArrayList<>();
        CanonicalType profile = new CanonicalType();
        profile.setValue("https://example.org/fhir/krcore/StructureDefinition/krcore-patient");
        profileList.add(profile);
        meta.setProfile(profileList);
        patient.setMeta(meta);

        Narrative narrative = new Narrative();
        narrative.setStatus(Narrative.NarrativeStatus.GENERATED);
        patient.setText(narrative);

        List<Identifier> identifierList = new ArrayList<>();
        Identifier identifier = new Identifier();
        CodeableConcept codeableConcept = new CodeableConcept();
        Coding coding = new Coding();
        coding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");
        coding.setCode("MR");
        codeableConcept.addCoding(coding);
        identifier.setType(codeableConcept);
        identifier.setSystem("urn:oid:1.2.3.4.5.6");
        identifier.setValue("MR12345");
        identifierList.add(identifier);
        patient.setIdentifier(identifierList);

        HumanName humanName = new HumanName();
        humanName.setText("김환자");
        List<HumanName> humanNameList = new ArrayList<>();
        humanNameList.add(humanName);
        patient.setName(humanNameList);

        ContactPoint telecom = new ContactPoint();
        telecom.setSystem(ContactPoint.ContactPointSystem.PHONE);
        telecom.setValue("010-1234-5678");
        patient.addTelecom(telecom);

        patient.setGender(Enumerations.AdministrativeGender.MALE);

        Calendar instance = Calendar.getInstance();
        instance.set(2001, Calendar.JANUARY, 1);
        patient.setBirthDate(instance.getTime());

        Address address = new Address();

        List<Extension> addressExtensionList = new ArrayList<>();

        StringType districtValue = new StringType();
        districtValue.setValue("서울특별시 강남구");
        Extension district = new Extension("district", districtValue);
        addressExtensionList.add(district);

        StringType roadNameValue = new StringType();
        roadNameValue.setValue("일원로");
        Extension roadName = new Extension("roadName", roadNameValue);
        addressExtensionList.add(roadName);

        StringType detailedAddressValue = new StringType();
        detailedAddressValue.setValue("81");
        Extension detailedAddress = new Extension("detailedAddress", detailedAddressValue);
        addressExtensionList.add(detailedAddress);

        StringType complementsValue = new StringType();
        complementsValue.setValue("일원동");
        Extension complements = new Extension("complements", complementsValue);
        addressExtensionList.add(complements);

        address.setText("서울특별시 강남구 일원로 81 (일원동)");
        address.setPostalCode("06351");
        address.setExtension(addressExtensionList);
        patient.addAddress(address);
        parser.setPrettyPrint(true);

        return ResponseEntity.ok(parser.encodeResourceToString(patient));
    }
}
