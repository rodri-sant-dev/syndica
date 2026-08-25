package com.syndica.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.syndica.backend.domain.dto.UserForCreateDTO;
import com.syndica.backend.domain.dto.UserResponseDTO;
import com.syndica.backend.domain.mappers.UserMapper;
import com.syndica.backend.domain.models.Group;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.domain.models.UserGroup;
import com.syndica.backend.domain.repositories.UserGroupRepository;
import com.syndica.backend.domain.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        UserGroupRepository userGroupRepository
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userGroupRepository = userGroupRepository;
    }

    public User saveUser(UserForCreateDTO userForCreateDTO){
        User user = User.builder()
            .passwordHash(passwordEncoder.encode(userForCreateDTO.password()))
            .cpf(userForCreateDTO.cpf())
            .fullname(userForCreateDTO.fullname())
            .email(userForCreateDTO.email())
            .build();
        
        return this.userRepository.save(user);
    }

    public void addGroup(User user, Group group){
        userGroupRepository.save(
            UserGroup.builder()
            .user(user)
            .group(group)
            .build()
        );
    }

    public UserResponseDTO getUser(UUID userId){
        return UserMapper.toUserResponseDTO(
            userRepository.getReferenceById(userId)
        );
    }
    
    public List<Group> listGroups(User user){
        return userRepository.findGroupsByUserId(user.getId());
    }

    public List<UserResponseDTO> listUsers(){
        return userRepository.findAll()
        .stream()
        .map(UserMapper::toUserResponseDTO)
        .toList();
    }

    @Transactional
    public void inactiveUser(UUID userId){
        User user = userRepository.getReferenceById(userId);
        if (user.isActive()) user.setActive(false);
    }

    @Transactional
    public void activeUser(UUID userId){
        User user = userRepository.getReferenceById(userId);
        if (!user.isActive()) user.setActive(true);
    }
}