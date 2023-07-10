package ru.findFood.menu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.findFood.menu.entities.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
