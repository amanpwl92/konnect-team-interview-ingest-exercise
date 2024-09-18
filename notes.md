# Important Points

1. ~~data in opensearch should go basis konnect entity id and updates handling should also be there~~
2. should we produce messages in batch or one by one
3. docker compose updates to run your programs as well - producer and consumer ?
4. ~~see if we can use avro for serialization/deserialization~~
5. could we use something like factory pattern (like we have in rm looker proc processor) to create different 
konnect objects from stream.jsonl ?
6. ~~create object from defined schema using kafka event at consumer side to be pushed to opensearch~~
7. failure handling during message producing/consuming.
   1. in case we read an entry from jsonl and face issue during parsing or pushing to kafka, we could write that
   record to some other jsonl file (stream-error.jsonl)
   2. similarly, if we face any error while consuming event , we could send it to some retry topic. We could also
   try if possible to add retry logic and backoff factor while consuming messages.
8. ~~do we need multiple indexes in open search or a unified index like we have data in file? Similarly, single topic
in kafka or multiple topics for each type on konnect entity.~~
9. which fields to be indexed in open search schema ? Right now we are utilizing OS default mappings to make fields
searchable, but we can explicitly change those mappings by defining some schema for OS index.
10. do we need to parse CDC stream key to derive something? Do we need to support event ordering here ?
11. logging in app, comments in code
12. any monitoring to see lags or any other metric ?
13. add unit test cases too.
14. at consumer side, we can maintain some data in map to store (entity id, updated_at of last event processed). This
map can help to fix out of order updated handling. We process only if updated_at of event > updated_at of event id from
map. Ideally this map data could be in some distributed key,value db like Redis.
15. we could use spring consumer which can have auto retry with backoff
16. multiple kafka topics are used for different schemas to support compatibility level "BACKWARD". We could use single 
topic by setting it as NONE too but that defeats purpose of avro schema and making schema changes backward compatible. 
One producer sending data to 3 topics. One consumer listening to data from those 3 topics.
to events from 3 topics. Schemas are varying and have less common fields so we did not create one unified schema. 
17. Add steps to run the app e2e (producer, consumer and also curl for compatibility none) and curls for other things like open search


# Understanding sample events schema and pattern

(fuzzy search on different Konnect entities (services, routes, nodes))
there are create/update events for different type of entities. No delete event in the sample

1. total events - 726
2. 8 events for cluster which have key like -> c/_global/o/cluster/f24150e5-4781-4d70-9350-aaa7700ee9c3 -> guid at
last is cluster id. The events are for create and update both for a cluster. Ex - cluster with guid
4c75f4f6-ca71-44a9-80ca-e96f6c412b24 has create and update event both.
3. 15 events for service - NEED TO CONSIDER
4. 554 events for node - NEED TO CONSIDER
5. 6 events for node-status - DONT THINK THIS IS NEEDED, BUT DOUBLE CHECK
6. 2 events for sni
7. 2 events for target
8. 14 events for route - NEED TO CONSIDER
9. 86 events for store_event
10. 1 event for key
11. 5 event for vault
12. 13 event for hash
13. 5 event for upstream
14. 2 event for composite-status
15. 5 event for consumer_group
16. 8 events for consumer

Count of events to be considered  = 15+554+14 = 583

# some working curls for opensearch

```
curl --location 'localhost:9200/cdc/_search?pretty=null&size=100' \
--header 'Content-Type: application/json'
```

```
curl --location --request GET 'http://localhost:9200/cdc/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "wildcard": {
      "object.name": {
        "value": "*namespace*"
      }
    }
  }
}'
```


```
curl --location --request GET 'http://localhost:9200/cdc/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "object.name": {
        "query": "gateway",
        "fuzziness": "AUTO"  
      }
    }
  }
}'

```
