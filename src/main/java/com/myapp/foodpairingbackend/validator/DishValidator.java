package com.myapp.foodpairingbackend.validator;

import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.service.DishService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class DishValidator {

    private final DishService dishService;

    public boolean validateDish(Dish dish) {
        boolean isDishNew = true;
        if (dishService.getDishByExternalSystemId(dish.getExternalSystemId()) != null) {
            isDishNew = false;
        }
        return isDishNew;
    }
}
