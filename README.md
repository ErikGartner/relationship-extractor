# Relationship Extractor
**Work in progress!**

*This scala program runs CoreNLP to extract basic relationships defined in a json such as "father", "mother" etc from a text.*

The purpose of this program is to extact relationship between persons (named entities) in a text.
Detection of named entities is automatic and the relationships are defined by keywords and wikidata IDs in ```data/relations.json```.

Currently we support english with the intention of extending that support to atleast another language, most likely Swedish.

## Usage
The Relationship Extractor uses SBT for dependencies management and as build tool.
By default it uses the CoreNLP server to speed up multiple calls.

- To get all dependencies the first time use ```sbt compile```
- Start the CoreNLP server by running ```./run_server.sh```
- Run the program ```sbt run``

## Contributors

- [Axel Larsson](https://github.com/AxelTLarsson)
- [Erik Gärtner](https://github.com/ErikGartner)