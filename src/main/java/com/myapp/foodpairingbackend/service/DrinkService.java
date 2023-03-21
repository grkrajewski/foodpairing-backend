package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.repository.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;

    public List<Drink> getDrinks() {
        return drinkRepository.findAll();
    }

    public Drink getDrink(final Long drinkId) throws ComponentNotFoundException {
        return drinkRepository.findById(drinkId)
                .orElseThrow(() -> new ComponentNotFoundException(ComponentNotFoundException.DRINK));
    }

    public void deleteDrink(final Long drinkId) {
        drinkRepository.deleteById(drinkId);
    }

    public Drink saveDrink(final Drink drink) {
        return drinkRepository.save(drink);
    }
}
