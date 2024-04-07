package com.ocado.basket;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BasketSplitterTest {

    @Test
    void splitBasket() {
        // Given
        String absolutePathToConfigFile = "config.json";
        String basketFilePath = "basket-3.json";
        BasketSplitter splitter = new BasketSplitter(absolutePathToConfigFile);
        List<String> basket = splitter.readBasket(basketFilePath);

        // When
        Map<String, List<String>> deliveryMap = splitter.split(basket);

        // Then
        assertEquals(2, deliveryMap.size());
        assertEquals(List.of("Espresso Machine", "Garden Chair"), deliveryMap.get("Courier"));
        assertEquals(List.of("Steak (300g)", "Carrots (1kg)", "Cold Beer (330ml)", "AA Battery (4 Pcs.)"), deliveryMap.get("Express Delivery"));
    }

    @Test
    void readBasket() {
        // Given
        BasketSplitter splitter = new BasketSplitter("config.json");
        String basketFilePath = "test_basket.json";
        List<String> expectedBasketItems = List.of("Item1", "Item2", "Item3");

        // When
        List<String> actualBasketItems = splitter.readBasket(basketFilePath);

        // Then
        assertEquals(expectedBasketItems, actualBasketItems);
    }

    @Test
    void readBasketWillThrowWhenInputIsInvalid() {
        // Given
        BasketSplitter splitter = new BasketSplitter("config.json");
        String nonExistentFilePath = "non_existent_file.json";

        // When/Then
        assertThrows(RuntimeException.class, () -> splitter.readBasket(nonExistentFilePath));
    }

    @Test
    void readProductDeliveryMethods() {
        // Given
        BasketSplitter splitter = new BasketSplitter("test_config.json");
        Map<String, List<String>> expectedDeliveryMethods = Map.of(
                "Product1", List.of("Method1", "Method2"),
                "Product2", List.of("Method3", "Method4")
        );

        // When
        Map<String, List<String>> actualDeliveryMethods = splitter.readProductDeliveryMethods("test_config.json");

        // Then
        assertEquals(expectedDeliveryMethods, actualDeliveryMethods);
    }

    @Test
    void readProductDeliveryMethodsWillThrowWhenInputIsInvalid() {
        // Given
        BasketSplitter splitter = new BasketSplitter("test_config.json");
        String nonExistentFilePath = "non_existent_file.json";

        // When/Then
        assertThrows(RuntimeException.class, () -> splitter.readProductDeliveryMethods(nonExistentFilePath));
    }
}