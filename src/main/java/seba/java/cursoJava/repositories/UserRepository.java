package seba.java.cursoJava.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import seba.java.cursoJava.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
}
