package com.taeheon.fhir.common.model;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface FhirModel {
    IBaseResource objectToResource();
}
