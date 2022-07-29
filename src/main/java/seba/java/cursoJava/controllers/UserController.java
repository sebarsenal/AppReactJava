package seba.java.cursoJava.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import seba.java.cursoJava.models.request.UserDetailsRequestModel;
import seba.java.cursoJava.models.response.UserRest;
import seba.java.cursoJava.services.UserServiceInterface;
import seba.java.cursoJava.shared.dto.UserDTO;

@RestController
@RequestMapping("/users") // localhost:8080/users
public class UserController {

    @Autowired
    UserServiceInterface userService;

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        UserDTO userDTO = userService.getUser(email);
        UserRest userToReturn = new UserRest();
        BeanUtils.copyProperties(userDTO, userToReturn);
        return userToReturn;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {

        UserRest userToReturn  = new UserRest();
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetails, userDTO);
        UserDTO createUser = userService.createUser(userDTO);
        BeanUtils.copyProperties(createUser, userToReturn);

        return userToReturn;
    }
}
