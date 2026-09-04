package com.syndica.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.syndica.backend.domain.dto.UserForCreateDTO;
import com.syndica.backend.domain.repositories.UserRepository;
import com.syndica.backend.service.UserService;

import lombok.extern.slf4j.Slf4j;


@Component
@Profile("dev")
@ConditionalOnProperty(name = "app.active-admin-user", havingValue = "true")
@Slf4j
public class AdminUserSeeder implements CommandLineRunner {

    @Value("${app.admin-password}")
    private String adminPassword;

    private final UserService userService;
    private final UserRepository userRepository;

    public AdminUserSeeder(
        UserService userService,
        UserRepository userRepository
    ){
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("AdminUserSeeder is started");

        if (!userRepository.existsByEmail("admin@admin.com")) {
            userService.saveUser(
                UserForCreateDTO
                    .builder()
                    .fullname("administrador do sistema")
                    .email("admin@admin.com")
                    .cpf("63810678023")
                    .password(adminPassword)
                    .build()
            );
            log.info("Admin User was created"); 
        }
    }
}
