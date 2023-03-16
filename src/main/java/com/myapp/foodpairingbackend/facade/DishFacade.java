package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.DishDto;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.*;
import com.myapp.foodpairingbackend.mapper.DishMapper;
import com.myapp.foodpairingbackend.service.DishService;
import com.myapp.foodpairingbackend.validator.DishValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DishFacade {

    private final DishService dishService;
    private final DishMapper dishMapper;
    private final DishValidator dishValidator;

    public List<DishDto> getDishes() {
        List<Dish> dishList = dishService.getDishes();
        return dishMapper.mapToDishDtoList(dishList);
    }

    public DishDto getDish(Long dishId) throws ComponentNotFoundException {
        Dish dish = dishService.getDish(dishId);
        return dishMapper.mapToDishDto(dish);
    }

    public void deleteDish(Long dishId) throws ComponentNotFoundException {
        try {
            dishService.deleteDish(dishId);
        } catch (DataAccessException e) {
            throw new ComponentNotFoundException(ComponentNotFoundException.DISH);
        }
    }

    public DishDto saveDishInDb(DishDto dishDto) throws ComponentNotFoundException, IdException, ComponentExistsException {
        if (dishDto.getId() == null) {
            Dish dish = dishMapper.mapToDish(dishDto);
            DishDto mappedDish;
            boolean isDishNew = dishValidator.validateDish(dish);
            if (isDishNew) {
                Dish savedDish = dishService.saveDish(dish);
                mappedDish = dishMapper.mapToDishDto(savedDish);
            } else {
                throw new ComponentExistsException(ComponentExistsException.DISH_EXISTS);
            }
            return mappedDish;
        }
        throw new IdException(IdException.ID_FOUND);
    }

    public DishDto updateDish(DishDto dishDto) throws ComponentNotFoundException, IdException {
        if (dishDto.getId() != null && dishService.getDish(dishDto.getId()) != null) {
            Dish dish = dishMapper.mapToDish(dishDto);
            Dish savedDish = dishService.saveDish(dish);
            return dishMapper.mapToDishDto(savedDish);
        }
        throw new IdException(IdException.ID_NOT_FOUND);
    }
}
