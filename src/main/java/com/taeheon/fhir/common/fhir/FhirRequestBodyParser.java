package com.taeheon.fhir.common.fhir;

import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.XmlParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class FhirRequestBodyParser implements HandlerMethodArgumentResolver {

    private final JsonParser jsonParser;
    private final XmlParser xmlParser;

    public FhirRequestBodyParser(JsonParser jsonParser, XmlParser xmlParser) {
        this.jsonParser = jsonParser;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FhirBody.class)
                && IBaseResource.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        ServletServerHttpRequest httpRequest = new ServletServerHttpRequest(httpServletRequest);
        MediaType mediaType = MediaType.valueOf(httpServletRequest.getContentType());
        return switch (mediaType.getSubtype()) {
            case "json", "fhir+json" -> jsonParser.parseResource(
                    parameter.getParameterType().asSubclass(IBaseResource.class),
                    httpRequest.getBody()
            );
            case "xml", "fhir+xml" -> xmlParser.parseResource(
                    parameter.getParameterType().asSubclass(IBaseResource.class),
                    httpRequest.getBody()
            );
            default -> throw new HttpMediaTypeNotSupportedException(httpServletRequest.getContentType());
        };
    }
}
