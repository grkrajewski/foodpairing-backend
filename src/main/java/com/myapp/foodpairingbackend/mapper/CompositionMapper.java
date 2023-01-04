package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.domain.dataprovider.DishProvider;
import com.myapp.foodpairingbackend.domain.dataprovider.DrinkProvider;
import com.myapp.foodpairingbackend.domain.dto.CompositionDto;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.exception.CommentNotFoundException;
import com.myapp.foodpairingbackend.exception.CompositionNotFoundException;
import com.myapp.foodpairingbackend.exception.DishNotFoundException;
import com.myapp.foodpairingbackend.exception.DrinkNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompositionMapper {

    private final DishProvider dishProvider;
    private final DrinkProvider drinkProvider;
    private final CommentMapper commentMapper;

    public Composition mapToComposition(final CompositionDto compositionDto) throws DishNotFoundException,
            DrinkNotFoundException, CompositionNotFoundException, CommentNotFoundException {
        Composition composition = Composition.builder()
                .id(compositionDto.getId())
                .dish(dishProvider.fetchDish(compositionDto))
                .drink(drinkProvider.fetchDrink(compositionDto))
                .created(compositionDto.getCreated())
                .commentList(commentMapper.mapToCommentList(compositionDto.getCommentList()))
                .build();
        return composition;
    }

    public CompositionDto mapToCompositionDto(final Composition composition) {
        CompositionDto compositionDto = CompositionDto.builder()
                .id(composition.getId())
                .dishId(composition.getDish().getId())
                .drinkId(composition.getDrink().getId())
                .created(composition.getCreated())
                .commentList(commentMapper.mapToCommentDtoList(composition.getCommentList()))
                .build();
        return compositionDto;
    }

    public List<Composition> mapToCompositionList(final List<CompositionDto> compositionDtoList) throws DrinkNotFoundException,
            DishNotFoundException, CompositionNotFoundException, CommentNotFoundException {
        List<Composition> compositionList = new ArrayList<>();
        for (CompositionDto compositionDto : compositionDtoList) {
            Composition composition = mapToComposition(compositionDto);
            compositionList.add(composition);
        }
        return compositionList;
    }

    public List<CompositionDto> mapToCompositionDtoList(final List<Composition> compositionList) {
        return compositionList.stream()
                .map(this::mapToCompositionDto)
                .collect(Collectors.toList());
    }
}
