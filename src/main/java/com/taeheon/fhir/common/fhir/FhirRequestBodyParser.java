package com.taeheon.fhir.common.fhir;

import ca.uhn.fhir.parser.IParser;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

public class FhirRequestBodyParser implements HandlerMethodArgumentResolver {

    private final IParser parser;
    private final List<MediaType> supportedMediaTypeList;

    public FhirRequestBodyParser(IParser parser, MediaType... supportedMediaTypeList) {
        this.parser = parser;
        this.supportedMediaTypeList = List.of(supportedMediaTypeList);
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
        if (supportedMediaTypeList.stream().anyMatch(type -> type.equals(httpRequest.getHeaders().getContentType()))) {
            return parser.parseResource(
                    parameter.getParameterType().asSubclass(IBaseResource.class),
                    httpRequest.getBody()
            );
        } else {
            return null;
        }
    }
}
