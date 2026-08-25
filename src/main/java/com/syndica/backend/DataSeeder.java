package com.syndica.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.syndica.backend.domain.dto.UserForCreateDTO;
import com.syndica.backend.domain.models.Apartment;
import com.syndica.backend.domain.models.Group;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.domain.repositories.ApartmentRepository;
import com.syndica.backend.domain.repositories.GroupRepository;
import com.syndica.backend.service.UserService;

@Component
public class DataSeeder implements CommandLineRunner {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final ApartmentRepository apartmentRepository;
    

    public DataSeeder(
        GroupRepository groupRepository,
        UserService userService,
        ApartmentRepository apartmentRepository
    ) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public void run(String... args) {
        try{
            User user = userService.saveUser(
                UserForCreateDTO
                .builder()
                .username("root")
                .password("root")
                .cpf("12345678")
                .fullname("paulo braga")
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
            
            for(int j = 1; j <= 2; j++){
                for(int k = 1; k <= 16; k++){
                    apartmentRepository.save(
                        Apartment.builder()
                            .number(k)
                            .block(j)
                            .floor((k % 3) + 1)
                            .build()
                    );
                }
            }
        }catch(Exception  e){

        }
    }
}