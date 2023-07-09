package ru.findFood.menu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.findFood.menu.entities.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
