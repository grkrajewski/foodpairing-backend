package com.myapp.foodpairingbackend.repository;

import com.myapp.foodpairingbackend.domain.entity.Dish;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface DishRepository extends CrudRepository<Dish, Long> {

    @Override
    List<Dish> findAll();

    Dish findByExternalSystemId(Long id);
}
