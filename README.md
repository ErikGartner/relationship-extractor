# Relationship Extractor
*This scala program runs CoreNLP to extract basic relationships defined in a json such as "father", "mother" etc from a text. It was part of a university project course. See the [article](/Inferring%20Family%20Relationships%20from%20a%20Corpus.pdf) for more information.*

The purpose of this program is to extract relationships between persons (named entities) in a text.
Detection of named entities is automatic and the relationships are defined by keywords and wikidata IDs in ```data/relations.json```.

Currently we support english with the intention of extending that support to at least another language, most likely Swedish.

## Usage
The Relationship Extractor uses SBT for dependencies management and as a build tool.
By default it uses the CoreNLP server to speed up multiple calls.

- To get all dependencies the first time use ```sbt compile```
- Start the CoreNLP server by running ```./run_server.sh```
- Run the program ```sbt run```

## Contributors

- [Axel Larsson](https://github.com/AxelTLarsson)
- [Erik Gärtner](https://github.com/ErikGartner)
