package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    public List<Dish> getDishes() {
        return dishRepository.findAll();
    }

    public Dish getDish(final Long dishId) throws ComponentNotFoundException {
        return dishRepository.findById(dishId)
                .orElseThrow(() -> new ComponentNotFoundException(ComponentNotFoundException.DISH));
    }

    public Dish getDishByExternalSystemId(final Long dishExternalId) {
        return dishRepository.findByExternalSystemId(dishExternalId);
    }

    public void deleteDish(final Long dishId) throws ComponentNotFoundException {
        try {
            dishRepository.deleteById(dishId);
        } catch (DataAccessException e) {
            throw new ComponentNotFoundException(ComponentNotFoundException.DISH);
        }
    }

    public Dish saveDish(final Dish dish) throws IdException, ComponentExistsException {
        if (dish.getId() == null) {
            if (getDishByExternalSystemId(dish.getExternalSystemId()) == null) {
                return dishRepository.save(dish);
            }
            throw new ComponentExistsException(ComponentExistsException.DISH_EXISTS);
        }
        throw new IdException(IdException.ID_FOUND);
    }

    public Dish updateDish(final Dish dish) throws ComponentNotFoundException, IdException {
        if (dish.getId() != null && getDish(dish.getId()) != null) {
            return dishRepository.save(dish);
        }
        throw new IdException(IdException.ID_NOT_FOUND);
    }
}
