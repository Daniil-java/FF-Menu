package ru.findFood.menu.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.findFood.menu.dtos.DishDto;
import ru.findFood.menu.entities.Dish;
import ru.findFood.menu.exceprions.NotFoundException;
import ru.findFood.menu.repositories.DishesRepository;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishesService {
    private final DishesRepository dishesRepository;

    @Value("${dish.category.breakfast}")
    private String BREAKFAST;

    @Value("${dish.category.lunch}")
    private String LUNCH;

    @Value("${dish.category.dinner}")
    private String DINNER;

    @Value("${dish.category.snack}")
    private String SNACK;


    //Сделал тремя разными методами на случай если это будет три разных репозитория - мы еще не определились😁
    public List<Dish> findBreakfasts() {
        return dishesRepository.findByCategory(BREAKFAST);
    }

    public List<Dish> findDinners() {
        return dishesRepository.findByCategory(DINNER);
    }


    public List<Dish> findLunches() {
        return dishesRepository.findByCategory(LUNCH);
    }

    public List<Dish> findSnack() {
        return dishesRepository.findByCategory(SNACK);
    }


    @Transactional
    public void markDishesAsUsed(List<DishDto> dishes) {
        for (DishDto dishDto : dishes) {
            Optional<Dish> byId = dishesRepository.findById(dishDto.id());
            Dish dish = byId.orElseThrow(() -> new NotFoundException(String.format("The dish '%s' is not exist!", dishDto)));
            dish.setUsedLastTime(LocalDateTime.now());
            dishesRepository.save(dish);
        }
    }
}
