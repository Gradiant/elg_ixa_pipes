package com.example.elg.handler;

import eu.elg.handler.ElgHandler;
import eu.elg.handler.ElgMessageHandler;
import eu.elg.model.AnnotationObject;
import eu.elg.model.requests.TextRequest;
import eu.elg.model.responses.AnnotationsResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@ElgHandler
public class Handler {

    @ElgMessageHandler
    public AnnotationsResponse process(TextRequest request) throws Exception {
        return new AnnotationsResponse().withAnnotations("Hello",
                Arrays.asList(new AnnotationObject()
                        .withOffsets(0,1)
                        .withFeatures("hello", "world")));
    }
}