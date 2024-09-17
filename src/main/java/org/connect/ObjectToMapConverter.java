package org.connect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ObjectToMapConverter {

  public static Map<String, Object> convertToMap(Object obj) {
    Map<String, Object> result = new HashMap<>();
    Set<Object> visitedObjects = new HashSet<>();  // To track visited objects and prevent cycles
    convert(obj, result, visitedObjects);
    return result;
  }

  private static void convert(Object obj, Map<String, Object> map, Set<Object> visitedObjects) {
    if (obj == null || visitedObjects.contains(obj)) {
      return;
    }

    visitedObjects.add(obj);  // Mark the object as visited

    Class<?> objClass = obj.getClass();

    // Handle the case for collections (List, Set, etc.)
    if (obj instanceof Iterable) {
      int index = 0;
      for (Object item : (Iterable<?>) obj) {
        map.put(String.valueOf(index++), convertToMap(item));
      }
      return;
    }

    // Handle the case for arrays
    if (obj.getClass().isArray()) {
      int length = java.lang.reflect.Array.getLength(obj);
      for (int i = 0; i < length; i++) {
        map.put(String.valueOf(i), convertToMap(java.lang.reflect.Array.get(obj, i)));
      }
      return;
    }

    // Process the fields of the object
    for (Field field : objClass.getDeclaredFields()) {
      field.setAccessible(true);  // Allow access to private fields
      try {
        Object value = field.get(obj);
        if (isSimpleValueType(value)) {
          map.put(field.getName(), value);
        } else {
          Map<String, Object> nestedMap = new HashMap<>();
          convert(value, nestedMap, visitedObjects);  // Recursively convert nested objects
          map.put(field.getName(), nestedMap);
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();  // Handle exceptions as needed
      }
    }
  }

  private static boolean isSimpleValueType(Object value) {
    return value == null || value instanceof String || value instanceof Number || value instanceof Boolean;
  }
}


