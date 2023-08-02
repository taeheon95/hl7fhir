package com.taeheon.fhir.common.fhir;

import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.List;

public class FhirConverter implements HttpMessageConverter<IBaseResource> {

    private final IParser parser;
    private final List<MediaType> supportedMediaTypeList;


    public FhirConverter(IParser parser, MediaType ...mediaTypeList) {
        this.parser = parser;
        this.supportedMediaTypeList = List.of(mediaTypeList);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return clazz.isAssignableFrom(IBaseResource.class)
                && supportedMediaTypeList.stream().anyMatch(type -> type.equals(mediaType));
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return IBaseResource.class.isAssignableFrom(clazz)
                && supportedMediaTypeList.stream().anyMatch(type -> type.equals(mediaType));
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypeList;
    }

    @Override
    public IBaseResource read(Class<? extends IBaseResource> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(IBaseResource iBaseResource, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().setContentType(supportedMediaTypeList.get(0));
        outputMessage.getBody().write(parser.encodeResourceToString(iBaseResource).getBytes());
    }
}
