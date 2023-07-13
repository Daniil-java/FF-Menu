package ru.findFood.menu.utils;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.findFood.menu.entities.Category;
import ru.findFood.menu.entities.Dish;
import ru.findFood.menu.entities.Restaurant;
import ru.findFood.menu.repositories.CategoryRepository;
import ru.findFood.menu.repositories.DishesRepository;
import ru.findFood.menu.repositories.RestaurantRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataGenerator {

    @Value("${dish.category.breakfast}")
    private String BREAKFAST;

    @Value("${dish.category.lunch}")
    private String LUNCH;

    @Value("${dish.category.dinner}")
    private String DINNER;

    @Value("${dish.category.snack}")
    private String SNACK;

    private final static int ORIGIN = 0;
    private final static int BOUND = 50;

    private final DishesRepository dishesRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    //Запускаем этот класс, когда нужно заполнить базу сгенерированными данными.
    @PostConstruct
    @Transactional
    public void generateData() {
        //Создаем рестораны
        Restaurant restaurant1 = new Restaurant("Диетолог");
        restaurantRepository.save(restaurant1);
        Restaurant restaurant2 = new Restaurant("Вкусно - и точка");
        restaurantRepository.save(restaurant2);
        Restaurant restaurant3 = new Restaurant("KFC");
        restaurantRepository.save(restaurant3);
        Restaurant restaurant4 = new Restaurant("Burger King");
        restaurantRepository.save(restaurant4);

        List<Restaurant> restaurants = restaurantRepository.findAll();

        //Создаем категории
        Category breakfast = new Category(BREAKFAST);
        categoryRepository.save(breakfast);
        Category lunch = new Category(LUNCH);
        categoryRepository.save(lunch);
        Category dinner = new Category(DINNER);
        categoryRepository.save(dinner);
        Category snack = new Category(SNACK);
        categoryRepository.save(snack);

        List<Category> categories = categoryRepository.findAll();


        //Заполняем базу блюд. Меняем число итераций на нужное в базе количество строк
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
//          necessary
            Dish dish = new Dish(
                    faker.food().dish(),
                    random.nextInt(ORIGIN, BOUND),
                    random.nextInt(ORIGIN, BOUND),
                    random.nextInt(ORIGIN, BOUND),
                    categories.get(random.nextInt(0, categories.size())));

//          unnecessary
            dish
                    .setApproved(false)
                    .setHealthy(false)
                    .setPrice(BigDecimal.valueOf(random.nextInt(79, 701)))
                    .setRestaurant(restaurants.get(random.nextInt(0, restaurants.size())))
                    .setDescription(getQuote(faker));
            dishesRepository.save(dish);
        }
    }

    private static String getQuote(Faker faker) {
        String quote = faker.hitchhikersGuideToTheGalaxy().quote();
        if (quote.length() > 255) quote = quote.substring(0, 255);
        return quote;
    }
}


