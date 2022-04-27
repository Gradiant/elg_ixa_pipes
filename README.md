# ``ixa-pipes-pos``

IXA pipes is a modular set of Natural Language Processing tools (or pipes) which provide easy access to NLP technology for several languages.
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

