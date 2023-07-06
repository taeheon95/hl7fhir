package com.taeheon.fhir.patient.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class PatientServiceTest {

    final Logger log = LoggerFactory.getLogger(this.getClass());
    static FhirContext fhirContext = FhirContext.forR5();
    static IParser xmlParser = fhirContext.newXmlParser();
    static IParser jsonParser = fhirContext.newJsonParser();

    @BeforeAll
    static void beforeAll() {
        xmlParser.setPrettyPrint(true);
    }

    @Test
    public void patientTest() {
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
        XhtmlNode xhtmlType = new XhtmlNode();
        narrative.setDiv(xhtmlType);
        narrative.setStatus(Narrative.NarrativeStatus.GENERATED);
        patient.setText(narrative);

        ContactPoint telecom = new ContactPoint();
        telecom.setSystem(ContactPoint.ContactPointSystem.PHONE);
        telecom.setValue("010-0000-0000");
        patient.addTelecom(telecom);

        patient.setGender(Enumerations.AdministrativeGender.MALE);

        Calendar instance = Calendar.getInstance();
        instance.set(2022, Calendar.SEPTEMBER, 12);
        patient.setBirthDate(instance.getTime());

        Address address = new Address();
        address.setText("부산시 운전구 어디동 어디 아파트 어디");
        address.setPostalCode("10101");
        patient.addAddress(address);

        String patientXml = xmlParser.encodeResourceToString(patient);
        log.info("\n{}",patientXml);
    }

}