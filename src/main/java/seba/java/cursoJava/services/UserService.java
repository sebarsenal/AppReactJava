package seba.java.cursoJava.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import seba.java.cursoJava.UserRepository;
import seba.java.cursoJava.entities.UserEntity;
import seba.java.cursoJava.shared.dto.UserDTO;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO user) {

        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new RuntimeException("El email ya está en uso");

        //Lógica para crear usuario
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        userEntity.setEncryptedPassword("testpass");
        userEntity.setUserId("testidpublico");

        UserEntity storedUserDetails = userRepository.save(userEntity);

        userRepository.save(userEntity);

        UserDTO userToReturn = new UserDTO();
        BeanUtils.copyProperties(storedUserDetails, userToReturn);

        return userToReturn;
    }

}
