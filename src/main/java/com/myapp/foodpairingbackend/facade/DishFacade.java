package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.DishDto;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.*;
import com.myapp.foodpairingbackend.mapper.DishMapper;
import com.myapp.foodpairingbackend.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DishFacade {

    private final DishService dishService;
    private final DishMapper dishMapper;

    public List<DishDto> getDishes() {
        List<Dish> dishList = dishService.getDishes();
        return dishMapper.mapToDishDtoList(dishList);
    }

    public DishDto getDish(Long dishId) throws ComponentNotFoundException {
        Dish dish = dishService.getDish(dishId);
        return dishMapper.mapToDishDto(dish);
    }

    public void deleteDish(Long dishId) throws ComponentNotFoundException {
        dishService.deleteDish(dishId);
    }

    public DishDto saveDishInDb(DishDto dishDto) throws ComponentNotFoundException, IdException, ComponentExistsException {
        Dish dish = dishMapper.mapToDish(dishDto);
        Dish savedDish = dishService.saveDish(dish);
        return dishMapper.mapToDishDto(savedDish);
    }

    public DishDto updateDish(DishDto dishDto) throws ComponentNotFoundException, IdException {
        Dish dish = dishMapper.mapToDish(dishDto);
        Dish savedDish = dishService.updateDish(dish);
        return dishMapper.mapToDishDto(savedDish);
    }
}
