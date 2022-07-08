package seba.java.cursoJava.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import seba.java.cursoJava.shared.dto.UserDTO;

public interface UserServiceInterface extends UserDetailsService{
    public UserDTO createUser(UserDTO user);

}
