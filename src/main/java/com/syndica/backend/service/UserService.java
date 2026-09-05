package com.syndica.backend.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
import com.syndica.backend.domain.models.UserGroup;
import com.syndica.backend.domain.models.UserOutbox;
import com.syndica.backend.domain.repositories.UserGroupRepository;
import com.syndica.backend.domain.repositories.UserOutboxRepository;
import com.syndica.backend.domain.repositories.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserOutboxRepository userOutboxRepository;
    private final DocumentService documentService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        DocumentService documentService,
        UserOutboxRepository userOutboxRepository,
        PasswordEncoder passwordEncoder,
        UserGroupRepository userGroupRepository
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userGroupRepository = userGroupRepository;
        this.documentService = documentService;
        this.userOutboxRepository = userOutboxRepository;
    }


    @Transactional
    public void saveUser(
        UserForCreateDTO userForCreateDTO
    ) throws IOException {

        userRepository.save(
            UserMapper.fromUserForCreateDTOToUser(
                userForCreateDTO,
                passwordEncoder.encode(
                    userForCreateDTO.password()
                )
            )
        );   
             
    }

    @Transactional
    public void saveUser(
        MultipartFile file,
        UserForCreateDTO userForCreateDTO
    ) throws IOException {

        Document document = documentService.uploadLocalDocument(file);

        User user = userRepository.save(
            UserMapper.fromUserForCreateDTOToUser(
                userForCreateDTO,
                passwordEncoder.encode(
                    userForCreateDTO.password()
                )
            )
        );
        user.setAvatarImage(document);
        userRepository.save(user);

        userOutboxRepository.save(
            UserOutbox
            .builder()
            .user(user)
            .eventType("CREATE_USER")
            .payload(Map.of(
                "id", document.getId().toString()
            ))
            .status("PENDING")
            .build()
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