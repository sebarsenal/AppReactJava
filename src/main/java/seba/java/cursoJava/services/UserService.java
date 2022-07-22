package seba.java.cursoJava.services;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import seba.java.cursoJava.entities.UserEntity;
import seba.java.cursoJava.repositories.UserRepository;
import seba.java.cursoJava.shared.dto.UserDTO;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDTO createUser(UserDTO user) {

        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new RuntimeException("El email ya está en uso");

        // Logica para crear usuario
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        userEntity.setEncryptedPassword(
                bCryptPasswordEncoder.encode(user.getPassword()));

        UUID userId = UUID.randomUUID();
        userEntity.setUserId(userId.toString());

        UserEntity storedUserDetails = userRepository.save(userEntity);

        userRepository.save(userEntity);

        UserDTO userToReturn = new UserDTO();
        BeanUtils.copyProperties(storedUserDetails, userToReturn);

        return userToReturn;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDTO getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDTO userToReturn = new UserDTO();
        BeanUtils.copyProperties(userEntity, userToReturn);
        return userToReturn;
    }

}
