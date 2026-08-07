package com.syndica.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.syndica.backend.domain.dto.UserForCreateDTO;
import com.syndica.backend.domain.repositories.UserRepository;
import com.syndica.backend.domain.models.User;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void saveUser(UserForCreateDTO userForCreateDTO){
        User user = User.builder()
            .username(userForCreateDTO.username())
            .passwordHash(passwordEncoder.encode(userForCreateDTO.password()))
            .cpf(userForCreateDTO.cpf())
            .build();
        
        this.userRepository.save(user);
    }
}
