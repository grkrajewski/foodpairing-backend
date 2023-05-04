package com.myapp.foodpairingbackend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name ="COMPOSITIONS")
public class Composition {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "DISH_ID")
    private Dish dish;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "DRINK_ID")
    private Drink drink;

    @NotNull
    @Column(name = "CREATED")
    private LocalDateTime created;

    @OneToMany(
            targetEntity = Comment.class,
            mappedBy = "composition",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Comment> commentList;
}
