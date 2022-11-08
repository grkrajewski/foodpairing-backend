package com.myapp.foodpairingbackend.repository;

import com.myapp.foodpairingbackend.domain.entity.Reaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ReactionRepository extends CrudRepository<Reaction, Long> {

    @Override
    List<Reaction> findAll();
}
