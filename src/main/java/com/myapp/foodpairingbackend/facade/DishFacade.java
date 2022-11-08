package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.DishDto;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.CommentNotFoundException;
import com.myapp.foodpairingbackend.exception.CompositionNotFoundException;
import com.myapp.foodpairingbackend.exception.DishNotFoundException;
import com.myapp.foodpairingbackend.exception.DrinkNotFoundException;
import com.myapp.foodpairingbackend.mapper.DishMapper;
import com.myapp.foodpairingbackend.service.DishService;
import com.myapp.foodpairingbackend.validator.DishValidator;
import lombok.RequiredArgsConstructor;
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

    public DishDto getDish(Long dishId) throws DishNotFoundException {
        Dish dish = dishService.getDish(dishId);
        return dishMapper.mapToDishDto(dish);
    }

    public void deleteDish(Long dishId) {
        dishService.deleteDish(dishId);
    }

    public DishDto saveDishInDb(DishDto dishDto) throws DrinkNotFoundException,  DishNotFoundException,
            CompositionNotFoundException, CommentNotFoundException {
        Dish dish = dishMapper.mapToDish(dishDto);
        DishDto mappedDish = null;
        boolean isBookNew = dishValidator.validateDish(dish);
        if (isBookNew) {
            Dish savedDish = dishService.saveDish(dish);
            mappedDish = dishMapper.mapToDishDto(savedDish);
        }
        return mappedDish;
    }

    public DishDto updateDish(DishDto dishDto) throws DrinkNotFoundException,  DishNotFoundException,
            CompositionNotFoundException, CommentNotFoundException {
        Dish dish = dishMapper.mapToDish(dishDto);
        Dish savedDish = dishService.saveDish(dish);
        return dishMapper.mapToDishDto(savedDish);
    }
}
