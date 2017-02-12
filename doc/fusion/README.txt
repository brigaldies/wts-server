*** Installation:

ATTENTION: Unzip the Fusion zipped download with 7-Zip. The Windows default unzip does not work for the very large
Fusion download.

*** Start and Stop from the Fusion installation directory:

Start Fusion: bin\fusion start
Stop Fusion: bin\fusion start

Fusion's ports:
zookeeper : 9983
solr      : 8983
api       : 8765
connectors: 8984
ui        : 8764

*** REST API:

*** Parser, Index, Fields, and Searching for the Underground Weather feed

** Parser:
Stages:
JSON
Fallback

JSON: Root path=hourly_forecast
Split arrays: Yes ('hourly_forecast' is an array in the underground weather Json).

** Index pipeline:
Field Mapping
Solr Dynamic Field Name Mapping
Solr Indexer

** Fields
Create a static field "temp.english" of type INT to convert the underground weather temp.english text field to an integer.

** Searching
Examples:
http://localhost:8764/api/apollo/solr/weather/select?q=parent_s:*VA/Charlottesville*%20AND%20temp.english:[60%20TO%20*]&wt=json&sort=FCTTIME.epoch_dt%20asc&start=0&rows=100
Collection: weather
Query (Retrieve Charlottesville, VA temperatures >= 60: q=parent_s:*VA/Charlottesville*%20AND%20temp.english:[60%20TO%20*]
Returned JSON format: wt=json
Order results by ascending epoch (Date and time stamp in Zulu time): sort=FCTTIME.epoch_dt%20asc
Page size (100 records): start=0&rows=100
