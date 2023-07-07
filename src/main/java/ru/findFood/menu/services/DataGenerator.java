package ru.findFood.menu.services;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.findFood.menu.entities.Category;
import ru.findFood.menu.entities.Dish;
import ru.findFood.menu.entities.GroupDish;
import ru.findFood.menu.entities.Restaurant;
import ru.findFood.menu.repositories.CategoryRepository;
import ru.findFood.menu.repositories.DishesRepository;
import ru.findFood.menu.repositories.GroupDishRepository;
import ru.findFood.menu.repositories.RestaurantRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataGenerator {

    @Autowired
    private DishesRepository dishesRepository;
    @Autowired
    private GroupDishRepository groupDishRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    //Запускаем этот класс, когда нужно заполнить базу сгенерированными данными.
    @EventListener(ApplicationReadyEvent.class)
    public void generateData() {

        //Создаем рестораны
        Restaurant restaurant1 = new Restaurant("Диетолог",  LocalDateTime.now());
        restaurantRepository.save(restaurant1);
        Restaurant restaurant2 = new Restaurant("Вкусно - и точка",  LocalDateTime.now());
        restaurantRepository.save(restaurant2);
        Restaurant restaurant3 = new Restaurant("KFC",  LocalDateTime.now());
        restaurantRepository.save(restaurant3);
        Restaurant restaurant4 = new Restaurant("Burger King",  LocalDateTime.now());
        restaurantRepository.save(restaurant4);

        //Создаем категории
        Category breakfast = new Category("Завтрак", LocalDateTime.now());
        categoryRepository.save(breakfast);
        Category lunch = new Category("Обед", LocalDateTime.now());
        categoryRepository.save(lunch);
        Category dinner = new Category("Ужин", LocalDateTime.now());
        categoryRepository.save(dinner);
        Category snack = new Category("Перекус", LocalDateTime.now());
        categoryRepository.save(snack);

        //Создаем виды блюд
        GroupDish dessert = new GroupDish("Десерт", LocalDateTime.now());
        groupDishRepository.save(dessert);
        GroupDish drink = new GroupDish("Напиток", LocalDateTime.now());
        groupDishRepository.save(drink);
        GroupDish appetizer = new GroupDish("Закуска", LocalDateTime.now());
        groupDishRepository.save(appetizer);
        GroupDish salad = new GroupDish("Салат", LocalDateTime.now());
        groupDishRepository.save(salad);
        GroupDish soup = new GroupDish("Суп", LocalDateTime.now());
        groupDishRepository.save(soup);
        GroupDish mainCourse = new GroupDish("Второе", LocalDateTime.now());
        groupDishRepository.save(mainCourse);
        GroupDish garnish = new GroupDish("Гарнир", LocalDateTime.now());
        groupDishRepository.save(garnish);
        GroupDish fruits = new GroupDish("Фрукты", LocalDateTime.now());
        groupDishRepository.save(fruits);
        GroupDish vegetables = new GroupDish("Овощи", LocalDateTime.now());
        groupDishRepository.save(vegetables);

        //Заполняем базу блюд. Меняем число итераций на нужное в базе количество строк
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Dish dish = new Dish(faker.food().dish(), (int) faker.number().randomNumber(3, false), (int) faker.number().randomNumber(1, false), (int) faker.number().randomNumber(2, false), (int) faker.number().randomNumber(2, false), LocalDateTime.now());
            dish.setRestaurant(restaurantRepository.findById((long) random.nextInt(1, 5)).get());
            dish.setPrice(BigDecimal.valueOf(random.nextInt(79, 701)));;
            dish.setGroupDish(groupDishRepository.findById((long) random.nextInt(1, 10)).get());
            dish.setCategory(categoryRepository.findById((long) random.nextInt(1, 5)).get());
            dish.setDescription(faker.hobbit().quote());
            dishesRepository.save(dish);
        }
    }
}


