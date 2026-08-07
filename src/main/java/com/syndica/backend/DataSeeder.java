package com.syndica.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.syndica.backend.domain.dto.UserForCreateDTO;
import com.syndica.backend.domain.models.Group;
import com.syndica.backend.service.UserService;
import com.syndica.backend.domain.repositories.GroupRepository;

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
        if (groupRepository.count() == 0) {
            groupRepository.save(
                Group.builder()
                .name("admin")
                .description("Adminstrador do sistema")
                .build()
            );
            
            groupRepository.save(
                Group.builder()
                .name("sindico")
                .description( "Sindico do Condominio")
                .build()
            );

            groupRepository.save(
                Group.builder()
                .name("morador")
                .description( "Morador do Condominio")
                .build()
            );
        }

        userService.saveUser(
            UserForCreateDTO
            .builder()
            .username("root")
            .password("root")
            .cpf("12345678")
            .build()
        );
        
    }
}