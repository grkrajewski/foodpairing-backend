package com.myapp.foodpairingbackend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DRINKS")
public class Drink {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;

    @NotNull
    @Column(name = "EXTERNAL_SYSTEM_ID")
    private String externalSystemId;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Column(name = "ALCOHOLIC")
    private String alcoholic;

    @Column(name = "GLASS")
    private String glass;

    @Column(name = "INSTRUCTIONS")
    private String instructions;

    @NotNull
    @OneToMany(
            targetEntity = DrinkIngredient.class,
            mappedBy = "drink",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<DrinkIngredient> drinkIngredientList;
}
