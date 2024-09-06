package com.example.simplecrudbeanvalidation.repository;

import com.example.simplecrudbeanvalidation.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
