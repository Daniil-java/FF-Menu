package ru.findFood.menu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.findFood.menu.entities.Dish;

import java.util.List;

@Repository
public interface DishesRepository extends JpaRepository<Dish, Long> {


    //ума не приложу как не хардкодить лимит
    @Query("select d from Dish d where d.category.title = ?1 ORDER BY d.usedLastTime asc limit 20")
    List<Dish> findByCategory(String category);


}
