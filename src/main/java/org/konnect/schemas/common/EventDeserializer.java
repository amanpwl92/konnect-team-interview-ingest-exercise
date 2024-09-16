package org.konnect.schemas.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.konnect.schemas.cdcevent.NodeEvent;
import org.konnect.schemas.cdcevent.RouteEvent;
import org.konnect.schemas.cdcevent.ServiceEvent;

import java.io.IOException;

public class EventDeserializer extends JsonDeserializer<EventValue> {

  @Override
  public EventValue deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode eventNode = mapper.readTree(p);  // This is the "after" node being deserialized

    // Extract the key field inside "after"
    JsonNode keyNode = eventNode.get("key");
    if (keyNode == null) {
      throw new RuntimeException("Missing 'key' field in 'after'.");
    }

    String key = keyNode.asText();

    // Extract the event type from the key (e.g., "node", "route", "service")
    String eventType = extractEventType(key);

    // Now, instead of accessing "after" again, treat eventNode as the event data itself
//    if (eventType.equals("service")) {
//      return mapper.treeToValue(eventNode, ServiceEvent.class);
//    } else if (eventType.equals("node")) {
//      return mapper.treeToValue(eventNode, NodeEvent.class);
//    } else if (eventType.equals("route")) {
//      return mapper.treeToValue(eventNode, RouteEvent.class);
//    }
    return null;

//    throw new RuntimeException("Unknown event type: " + eventType);
  }

  private String extractEventType(String key) {
    // Extract the type from the "key" (e.g., "node", "route", "service")
    String[] parts = key.split("/o/");
    if (parts.length > 1) {
      String[] eventParts = parts[1].split("/");
      return eventParts[0]; // node, route, service
    }
    throw new RuntimeException("Could not extract event type from key: " + key);
  }
}

