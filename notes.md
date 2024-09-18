# Important Points that could be addressed as enhancement

1. We could produce messages to kafka in batch instead one by one
2. Docker compose updates to run producer and consumer.
3. Which fields to be kept searchable in open search schema ? Right now we are utilizing opensearch default mappings 
to make fields searchable, but we can explicitly change those mappings by defining some schema for opensearch index.
4. Add unit test cases too.
5. At consumer side, we have a map to store (entity id, updated_at of last event processed) which is used to handle 
out of order updates. Ideally this map data could be in some distributed key,value db like Redis.
6. We could use spring consumer which can have auto retry with backoff.
7. Multiple kafka topics are used for different konnect entities to support compatibility level "BACKWARD". We could 
use single topic by setting it as NONE too but that defeats purpose of avro schema which helps in validating schema for
backward compatibility. Schemas of different konnect entities are varying and have less common fields so we did not 
create one unified schema.


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
curl --location --request GET 'http://localhost:9200/cdc/_search?pretty=null&size=100' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "wildcard": {
      "host": {
        "value": "*cypress*"
      }
    }
  }
}'
```


```
curl --location --request GET 'http://localhost:9200/cdc/_search?pretty=null&size=100' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "match": {
      "konnect_entity": {
        "query": "node",
        "fuzziness": "AUTO"  
      }
    }
  }
}'
```

```
curl --location --request DELETE 'localhost:9200/cdc' \
--header 'Content-Type: application/json'
```


