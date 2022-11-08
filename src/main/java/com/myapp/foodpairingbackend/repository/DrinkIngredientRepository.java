package com.myapp.foodpairingbackend.repository;

import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface DrinkIngredientRepository extends CrudRepository<DrinkIngredient, Long> {

    @Override
    List<DrinkIngredient> findAll();
}
