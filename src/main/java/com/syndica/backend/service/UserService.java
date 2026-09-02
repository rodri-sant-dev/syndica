package com.syndica.backend.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.syndica.backend.domain.dto.UserForCreateDTO;
import com.syndica.backend.domain.dto.UserResponseDTO;
import com.syndica.backend.domain.mappers.UserMapper;
import com.syndica.backend.domain.models.Document;
import com.syndica.backend.domain.models.Group;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.domain.models.UserDetails;
import com.syndica.backend.domain.models.UserGroup;
import com.syndica.backend.domain.repositories.UserDetailsRepository;
import com.syndica.backend.domain.repositories.UserGroupRepository;
import com.syndica.backend.domain.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final DocumentService documentService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsRepository userDetailsRepository;

    public UserService(
        UserRepository userRepository,
        UserDetailsRepository userDetailsRepository,
        DocumentService documentService,
        PasswordEncoder passwordEncoder,
        UserGroupRepository userGroupRepository
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userGroupRepository = userGroupRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.documentService = documentService;
    }

    @Transactional
    public void saveUser(
        MultipartFile file,
        UserForCreateDTO userForCreateDTO
    ) throws IOException {
        Document photoImageDocument = documentService.uploadDocument(file);
        
        UserDetails userDetails = userDetailsRepository.save(
            UserDetails.builder()
            .avatarImage(photoImageDocument)
            .themePreference(userForCreateDTO.theme())
            .build()
        );
        
        userRepository.save(
                UserMapper.fromUserForCreateDTOToUser(
                    userForCreateDTO,
                    passwordEncoder.encode(userForCreateDTO.password())
            )
        );
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