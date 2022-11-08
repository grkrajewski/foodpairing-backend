package com.myapp.foodpairingbackend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "COMMENTS")
public class Comment {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "CREATED")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "COMPOSITION_ID")
    private Composition composition;

    @OneToMany(
            targetEntity = Reaction.class,
            mappedBy = "comment",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Reaction> reactionList;
}
