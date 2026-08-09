package com.syndica.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.syndica.backend.domain.dto.UserForCreateDTO;
import com.syndica.backend.domain.models.Group;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.domain.repositories.GroupRepository;
import com.syndica.backend.service.UserService;

@Component
public class DataSeeder implements CommandLineRunner {

    private final GroupRepository groupRepository;
    private final UserService userService;

    public DataSeeder(
        GroupRepository groupRepository,
        UserService userService
    ) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        User user = userService.saveUser(
            UserForCreateDTO
            .builder()
            .username("root")
            .password("root")
            .cpf("12345678")
            .build()
        );

        userService.addGroup(
            user,
            groupRepository.save(
                Group.builder()
                .name("admin")
                .description("Adminstrador do sistema")
                .build()
            )
        );
        
        userService.addGroup(
            user,
            groupRepository.save(
                Group.builder()
                .name("Sindico")
                .description("Sindico do condominio")
                .build()
            )
        );
        
        userService.addGroup(
            user,
            groupRepository.save(
                Group.builder()
                .name("admin")
                .description("Morador do condominio")
                .build()
            )
        );
        

       
        
    }
}