package com.taeheon.fhir.common.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfig {
    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR5();
    }

    @Bean
    public IParser fhirJsonParser(FhirContext fhirContext) {
        return fhirContext.newJsonParser();
    }

}
