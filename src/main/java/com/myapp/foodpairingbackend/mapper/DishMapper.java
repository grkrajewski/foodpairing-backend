package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.domain.dto.DishDto;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.CommentNotFoundException;
import com.myapp.foodpairingbackend.exception.CompositionNotFoundException;
import com.myapp.foodpairingbackend.exception.DishNotFoundException;
import com.myapp.foodpairingbackend.exception.DrinkNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishMapper {

    private final CompositionMapper compositionMapper;

    public Dish mapToDish(final DishDto dishDto) throws DrinkNotFoundException, DishNotFoundException,
            CompositionNotFoundException, CommentNotFoundException {
        Dish dish = Dish.builder()
                .id(dishDto.getId())
                .externalSystemId(dishDto.getExternalSystemId())
                .name(dishDto.getName())
                .readyInMinutes(dishDto.getReadyInMinutes())
                .servings(dishDto.getServings())
                .recipeUrl(dishDto.getRecipeUrl())
                .compositionList(compositionMapper.mapToCompositionList(dishDto.getCompositionList()))
                .build();
        return dish;
    }

    public DishDto mapToDishDto(final Dish dish) {
        DishDto dishDto = DishDto.builder()
                .id(dish.getId())
                .externalSystemId(dish.getExternalSystemId())
                .name(dish.getName())
                .readyInMinutes(dish.getReadyInMinutes())
                .servings(dish.getServings())
                .recipeUrl(dish.getRecipeUrl())
                .compositionList(compositionMapper.mapToCompositionDtoList(dish.getCompositionList()))
                .build();
        return dishDto;
    }

    public List<DishDto> mapToDishDtoList(final List<Dish> dishList) {
        return dishList.stream()
                .map(this::mapToDishDto)
                .collect(Collectors.toList());
    }
}
