package com.ocado.basket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BasketSplitter {
    private final Map<String, List<String>> productDeliveryMethods;

    public BasketSplitter(String absolutePathToConfigFile) {
        this.productDeliveryMethods = readProductDeliveryMethods(absolutePathToConfigFile);
    }

    public Map<String, List<String>> split(List<String> items) {
        Map<String, List<String>> deliveryMap = new HashMap<>();

        while (!items.isEmpty()) {
            String currentlyMostUsedDeliveryMethod = findMostUsedDeliveryMethod(items);

            List<String> deliveryItems = items.stream()
                    .filter(item -> productDeliveryMethods.containsKey(item) && productDeliveryMethods.get(item).contains(currentlyMostUsedDeliveryMethod))
                    .collect(Collectors.toList());

            deliveryMap.put(currentlyMostUsedDeliveryMethod, deliveryItems);
            items.removeAll(deliveryItems);
        }

        return deliveryMap;
    }

    private String findMostUsedDeliveryMethod(List<String> items) {
        Map<String, Long> deliveryMethodCount = items.stream()
                .flatMap(item -> productDeliveryMethods.getOrDefault(item, Collections.emptyList()).stream())
                .collect(Collectors.groupingBy(
                        method -> method,
                        Collectors.counting()
                ));

        return Collections.max(deliveryMethodCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public Map<String, List<String>> readProductDeliveryMethods(String absolutePathToConfigFile) {
        try {
            JSONObject configObject = (JSONObject) parseJSONFileToObject(absolutePathToConfigFile);

            Map<String, List<String>> productDeliveryMethods = new HashMap<>();
            for (Object key : configObject.keySet()) {
                String productName = (String) key;
                JSONArray deliveryMethodsArray = (JSONArray) configObject.get(key);
                List<String> deliveryMethods = new ArrayList<>();
                for (Object method : deliveryMethodsArray) {
                    deliveryMethods.add((String) method);
                }
                productDeliveryMethods.put(productName, deliveryMethods);
            }
            return productDeliveryMethods;
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error reading config file: " + absolutePathToConfigFile, e);
        }
    }

    public List<String> readBasket(String basketFilePath) {
        try {
            JSONArray basketArray = (JSONArray) parseJSONFileToObject(basketFilePath);;
            List<String> basketItems = new ArrayList<>();
            
            for (Object item : basketArray) {
                basketItems.add((String) item);
            }
            
            return basketItems;
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error reading basket file: " + basketFilePath, e);
        }
    }

    private static Object parseJSONFileToObject(String absolutePathToConfigFile) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(absolutePathToConfigFile);

        return jsonParser.parse(reader);
    }
}
