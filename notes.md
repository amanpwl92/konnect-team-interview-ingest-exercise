# Important Points

1. data in opensearch should go basis konnect entity id and updates handling should also be there.
2. should we produce messages in batch or one by one
3. docker compose updates to run your programs as well - producer and consumer ?
4. should we create streaming app or separate programs for producer/consumer.
5. failure handling during message producing/consuming.
6. do we need multiple indexes in open search or a unified index like we have data in file? Similarly, single topic
in kafka or multiple topics for each type on konnect entity.
7. which fields to be indexed in open search schema ?
8. do we need to parse CDC stream key to derive something? Do we need to support event ordering here ?
9. logging in app
10. any monitoring to see lags or any other metric ?
11. add unit test cases too.


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
