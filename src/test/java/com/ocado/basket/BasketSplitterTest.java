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
}