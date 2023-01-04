package com.myapp.foodpairingbackend.domain.dataprovider;

import com.myapp.foodpairingbackend.domain.dto.CompositionDto;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.DishNotFoundException;
import com.myapp.foodpairingbackend.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DishProvider {

    private final DishService dishService;

    public Dish fetchDish(final CompositionDto compositionDto) throws DishNotFoundException {
        return dishService.getDish(compositionDto.getDishId());
    }
}
