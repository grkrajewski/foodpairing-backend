package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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

    public void deleteDrink(final Long drinkId) throws ComponentNotFoundException {
        try {
            drinkRepository.deleteById(drinkId);
        } catch (DataAccessException e) {
            throw new ComponentNotFoundException(ComponentNotFoundException.DRINK);
        }
    }

    public Drink saveDrink(final Drink drink) throws IdException {
        if (drink.getId() == null) {
            return drinkRepository.save(drink);
        }
        throw new IdException(IdException.ID_FOUND);
    }

    public Drink updateDrink(final Drink drink) throws ComponentNotFoundException, IdException {
        if (drink.getId() != null && getDrink(drink.getId()) != null) {
            return drinkRepository.save(drink);
        }
        throw new IdException(IdException.ID_NOT_FOUND);
    }
}
