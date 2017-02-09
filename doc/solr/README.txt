On a Windows machine:

Quick Start guide: http://lucene.apache.org/solr/quickstart.html

***** Start Solr *****

From a DOS command prompt:
1) Navigate to the Solr's installation directory.
2) Execute: bin/solr start -e cloud -noprompt

Access the Solr UI at http://localhost:8983/solr

REST API search, using the "gettingstarted" collection from the Quick Start:
http://localhost:8983/solr/gettingstarted/select?indent=on&q=*:*&wt=json
