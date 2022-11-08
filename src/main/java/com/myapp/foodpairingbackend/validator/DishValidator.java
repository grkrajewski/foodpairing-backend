package com.myapp.foodpairingbackend.validator;

import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.service.DishService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@RequiredArgsConstructor
public class DishValidator {

    private final DishService dishService;

    public boolean validateDish(Dish dish) {
        List<Dish> dbDishList = dishService.getDishes();
        boolean isBookNew = true;
        for (Dish dishInDb : dbDishList) {
            if (dish.equals(dishInDb)) {
                isBookNew = false;
            }
        }
        return isBookNew;
    }
}
