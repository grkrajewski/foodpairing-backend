package com.myapp.foodpairingbackend.dataprovider;

import com.myapp.foodpairingbackend.domain.dto.CompositionDto;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DishProvider {

    private final DishService dishService;

    public Dish fetchDish(final CompositionDto compositionDto) throws ComponentNotFoundException {
        return dishService.getDish(compositionDto.getDishId());
    }
}
