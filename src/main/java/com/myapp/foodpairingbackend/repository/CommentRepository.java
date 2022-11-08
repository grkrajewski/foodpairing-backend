package com.myapp.foodpairingbackend.repository;

import com.myapp.foodpairingbackend.domain.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Override
    List<Comment> findAll();
}
