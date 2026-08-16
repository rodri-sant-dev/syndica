package com.syndica.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syndica.backend.domain.dto.AllocateUserDTO;
import com.syndica.backend.domain.models.Apartment;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.domain.repositories.ApartmentRepository;
import com.syndica.backend.domain.repositories.UserRepository;
import com.syndica.backend.execptions.BusinessRuleException;
import com.syndica.backend.service.ApartmentService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/apartment")
public class ApartmentController {
    private final ApartmentService apartmentService;
    private final UserRepository userRepository;
    private final ApartmentRepository apartmentRepository;

    public ApartmentController(
        ApartmentService apartmentService,
        UserRepository userRepository,
        ApartmentRepository apartmentRepository
    ){
        this.apartmentService = apartmentService;
        this.userRepository = userRepository;
        this.apartmentRepository = apartmentRepository;

    }

    @GetMapping("")
    public String listAllAlocations() {
        return new String();
    }
    

    @PostMapping("allocate-user")
    public String allocateUser(@Valid @RequestBody AllocateUserDTO allocateUserDTO) {
        User user = userRepository.findById(allocateUserDTO.userId()).orElseThrow(
            () -> new BusinessRuleException("User Does Not Exist")
        );

        Apartment apartment = apartmentRepository.findById(allocateUserDTO.apartmentId())
        .orElseThrow(
            () -> new BusinessRuleException("Apartment Does Not Exist")
        );
        
        apartmentService.allocateUser(user, apartment);
        return "cadastrado";
    }

    @PostMapping("unallocate-user")
    public String unallocateUser(@RequestBody Long userApartmentID) {
        apartmentService.unallocateUser(userApartmentID);
        return "cadastrado";
    }

    

    
}
