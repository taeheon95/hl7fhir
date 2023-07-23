package com.taeheon.fhir.config;

import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.XmlParser;
import com.taeheon.fhir.common.fhir.FhirConverter;
import com.taeheon.fhir.common.fhir.FhirMediaTypes;
import com.taeheon.fhir.common.fhir.FhirRequestBodyParser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JsonParser jsonParser;
    private final XmlParser xmlParser;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new FhirRequestBodyParser(jsonParser, xmlParser));
//        resolvers.add(0, new FhirRequestBodyParser(jsonParser, MediaType.APPLICATION_JSON, FhirMediaTypes.FhirJson));
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new FhirConverter(xmlParser, MediaType.APPLICATION_XML, FhirMediaTypes.FhirXml));
        converters.add(0, new FhirConverter(jsonParser, MediaType.APPLICATION_JSON, FhirMediaTypes.FhirJson));
        WebMvcConfigurer.super.extendMessageConverters(converters);
    }
}
