package ru.findFood.menu.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.findFood.menu.dtos.DishDto;
import ru.findFood.menu.dtos.UpdateDishTimeRequest;
import ru.findFood.menu.integrations.RestaurantsServiceIntegration;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DishesService {
    private static final int DISH_QUERY_LIMIT = 50;

    private final RestaurantsServiceIntegration restaurantsSIP;


    @Value("${dish.category.breakfast}")
    private String breakfast;

    @Value("${dish.category.lunch}")
    private String lunch;

    @Value("${dish.category.dinner}")
    private String dinner;

    @Value("${dish.category.snack}")
    private String snack;

    @Value("${integrations.restaurants-service.querySize}")
    private String querySize;

    public List<DishDto> findBreakfasts() {
        System.out.println(querySize);
        System.out.println(breakfast);
        return restaurantsSIP.findByCategory(breakfast, querySize);
    }

    public List<DishDto> findDinners() {
        return restaurantsSIP.findByCategory(dinner, querySize);
    }

    public List<DishDto> findLunches() {
        return restaurantsSIP.findByCategory(lunch, querySize);
    }

    public List<DishDto> findSnack() {
        return restaurantsSIP.findByCategory(snack, querySize);
    }

    public void markDishesAsUsed(List<DishDto> dishes) {
        Map<Long, LocalDateTime> map = new HashMap<>();
        dishes.forEach(dishDto -> map.put(dishDto.id(), LocalDateTime.now()));
        restaurantsSIP.saveAll(new UpdateDishTimeRequest(map));
    }
}
