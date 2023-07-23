package com.taeheon.fhir.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.XmlParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfig {
    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR5();
    }

    @Bean
    public XmlParser fhirXmlParser(FhirContext fhirContext) {
        XmlParser parser = (XmlParser) fhirContext.newXmlParser();
        parser.setPrettyPrint(true);
        return parser;
    }

    @Bean
    public JsonParser fhirJsonParser(FhirContext fhirContext) {
        JsonParser parser = (JsonParser) fhirContext.newJsonParser();
        parser.setPrettyPrint(true);
        return parser;
    }

}
