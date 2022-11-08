package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.DishNotFoundException;
import com.myapp.foodpairingbackend.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    public List<Dish> getDishes() {
        return dishRepository.findAll();
    }

    public Dish getDish(final Long dishId) throws DishNotFoundException {
        return dishRepository.findById(dishId).orElseThrow(DishNotFoundException::new);
    }

    public void deleteDish(final Long dishId) {
        dishRepository.deleteById(dishId);
    }

    public Dish saveDish(final Dish dish) {
        return dishRepository.save(dish);
    }
}
