# ``ixa-pipes-pos``

IXA pipes is a modular set of Natural Language Processing tools (or pipes) which provide easy access to NLP technology for several languages. It offers robust and efficient linguistic annotation to both researchers and non-NLP experts with the aim of lowering the barriers of using NLP technology either for research purposes or for small industrial developers and SMEs. Here you can find an implementation of their Statistical Part of Speach (POS) tagging for Portuguese. 

# ELG repository

The repository with the implementation of the API can be found [here](https://github.com/Gradiant/elg_ixa_pipes)

## Install

```
sh docker-build.sh
```

## Run 

```
docker run --rm -p  0.0.0.0:8080:8080 --name ixa_pipes elg_ixa_pipes_pos:1.0
```

## Use

- Part of speach (POS) and Lemmatizer
  ```
  curl -X POST http://0.0.0.0:8080/process -H 'Content-Type: application/json' -d '{"type":"text", "content":"A criança brinca no jardim."}'
  ```

# Authors

## Original authors
This tool is developed by the [IXA NLP Group](http://ixa.si.ehu.es/) of the [University of the Basque Country](http://www.ehu.es/).

## ELG API integration

The ELG API was made by Gradiant R&D Centre (https://www.gradiant.org/) as part of the Connecting Europe Facility (CEF) project "Microservices at your service: bridging the gap between NLP research and industry" (https://ec.europa.eu/inea/en/connecting-europe-facility/cef-telecom/2020-eu-ia-0046)

Project website: https://www.lingsoft.fi/en/microservices-at-your-service-bridging-gap-between-nlp-research-and-industry

# Citation 

The original work of this tool is:
- Rodrigo Agerri, Josu Bermudez and German Rigau (2014): "IXA pipeline: Efficient and Ready to Use Multilingual NLP tools", in: Proceedings of the 9th Language Resources and Evaluation Conference (LREC2014), 26-31 May, 2014, Reykjavik, Iceland.
- https://ixa2.si.ehu.eus/ixa-pipes/