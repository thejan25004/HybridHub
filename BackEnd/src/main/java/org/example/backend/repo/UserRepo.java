package org.example.backend.repo;

import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByName(String name);

    User getUsersByEmail(String email);

    boolean existsUsersByEmail(String email);


}
