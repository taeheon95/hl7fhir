package com.taeheon.fhir.common.fhir;

import org.springframework.http.MediaType;

public interface FhirMediaTypes {
    MediaType FhirXml = MediaType.valueOf("application/fhir+xml");
    MediaType FhirJson = MediaType.valueOf("application/fhir+json");
}
