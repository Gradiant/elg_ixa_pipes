package com.example.elg.handler;

import eu.elg.handler.ElgHandler;
import eu.elg.handler.ElgMessageHandler;
import eu.elg.model.AnnotationObject;
import eu.elg.model.requests.TextRequest;
import eu.elg.model.responses.AnnotationsResponse;
import eus.ixa.ixa.pipe.tok.Annotate;
import ixa.kaflib.Entity;
import ixa.kaflib.KAFDocument;
import ixa.kaflib.Term;
import ixa.kaflib.WF;
import org.springframework.stereotype.Component;

import java.io.*;

import java.util.*;

import static com.google.common.base.Charsets.UTF_8;

@Component
@ElgHandler
public class Handler {

    @ElgMessageHandler
    public AnnotationsResponse process(TextRequest request) throws Exception {

        String lang = "eu";

        String content = request.getContent();
        BufferedReader inputString = new BufferedReader(new StringReader(content));
        //tok and pos models for create KAFDocument
        KAFDocument kaffromtok = createKAFDocumentfromTOKmodel(inputString);
        KAFDocument kaffrompos = runPOSModel(kaffromtok,lang);

        return createResponse(kaffrompos.getTerms());

    }

    public KAFDocument createKAFDocumentfromTOKmodel(BufferedReader breader) throws IOException {

        String lang = "eu"; //Vasco
        String kafversion = "v1.naf"; //Default

        //tokenizer
        Properties tok_properties = createtokProperties(lang);
        final eus.ixa.ixa.pipe.tok.Annotate tok_annotator = new Annotate(breader, tok_properties);
        KAFDocument kaf = new KAFDocument(lang, kafversion);
        final KAFDocument.LinguisticProcessor tokLp = kaf.addLinguisticProcessor(
                "text", "ixa-pipe-tok-notok-" + lang + "1.8.6");
        tok_annotator.tokenizeToKAF(kaf);

        return  kaf;

    }

    public KAFDocument runPOSModel(KAFDocument kaf,String lang) throws IOException {
        //pos
        final KAFDocument.LinguisticProcessor posLp = kaf.addLinguisticProcessor(
                "terms", "ixa-pipe-tok-notok-" + lang + "1.5.1");
        Properties pos_properties = createposProperties(kaf.getLang());
        final eus.ixa.ixa.pipe.pos.Annotate pos_annotator = new eus.ixa.ixa.pipe.pos.Annotate(pos_properties);
        pos_annotator.annotatePOSToKAF(kaf);
        return kaf;
    }

    private Properties createtokProperties(String lang) {
        //Set to default parameters
        Properties annotateProperties = new Properties();
        annotateProperties.setProperty("language", lang);
        annotateProperties.setProperty("normalize", "default");
        annotateProperties.setProperty("untokenizable", "no");
        annotateProperties.setProperty("hardParagraph", "no");
        annotateProperties.setProperty("noseg", "false");

        return annotateProperties;

    }

    private Properties createposProperties(String lang) {
        //Set to default parameters
        Properties annotateProperties = new Properties();
        annotateProperties.setProperty("language", lang);
        annotateProperties.setProperty("model", "models/ud-morph-models-1.5.0/eu/eu-pos-perceptron-ud.bin");
        annotateProperties.setProperty("lemmatizerModel", "models/ud-morph-models-1.5.0/eu/eu-lemma-perceptron-ud.bin");
        annotateProperties.setProperty("multiwords", "false");
        annotateProperties.setProperty("dictag", "false");

        return annotateProperties;
    }

    private AnnotationsResponse createResponse(List<Term> terms){
        List<AnnotationObject> annotation_list = new ArrayList<>();
        for (Term term : terms ){
            int start = 0;
            int end = 0;
            //features
            String lemma = term.getLemma();
            String morphofeat = term.getMorphofeat();
            String pos = term.getPos();
            for (WF wf: term.getSpan().getTargets()){
                start = wf.getOffset();
                end = wf.getLength() + start;
            }
            annotation_list.add(new AnnotationObject().withOffsets(start,end).withFeature("lemma",lemma).withFeature("morphofeat",morphofeat).withFeature("pos",pos));
        }
        return new AnnotationsResponse().withAnnotations("tokens",
                annotation_list);
    }
}