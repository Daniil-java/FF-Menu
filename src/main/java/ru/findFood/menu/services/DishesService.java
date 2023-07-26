package ru.findFood.menu.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.findFood.menu.dtos.DishDto;
import ru.findFood.menu.entities.Dish;
import ru.findFood.menu.repositories.DishesRepository;

import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishesService {
    private static final int DISH_QUERY_LIMIT = 50;

    private final DishesRepository dishesRepository;

    @Value("${dish.category.breakfast}")
    private String breakfast;

    @Value("${dish.category.lunch}")
    private String lunch;

    @Value("${dish.category.dinner}")
    private String dinner;

    @Value("${dish.category.snack}")
    private String snack;

    private Pageable pageable;


    @PostConstruct
    public void init() {
        pageable = PageRequest.of(0, DISH_QUERY_LIMIT);
    }

    public List<Dish> findBreakfasts() {
        return dishesRepository.findByCategory(breakfast, pageable);
    }

    public List<Dish> findDinners() {
        return dishesRepository.findByCategory(dinner, pageable);
    }

    public List<Dish> findLunches() {
        return dishesRepository.findByCategory(lunch, pageable);
    }

    public List<Dish> findSnack() {
        return dishesRepository.findByCategory(snack, pageable);
    }


    @Transactional
    public void markDishesAsUsed(List<DishDto> dishes) {
        List<Dish> dishList = dishesRepository.findByIdIn(
                dishes.stream()
                        .map(DishDto::id)
                        .toList());
        dishList.forEach(dish -> dish.setUsedLastTime(LocalDateTime.now()));
        dishesRepository.saveAll(dishList);
    }
}
