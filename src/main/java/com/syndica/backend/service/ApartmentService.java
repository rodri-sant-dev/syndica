package com.syndica.backend.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.syndica.backend.domain.models.Apartment;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.domain.models.UserApartment;
import com.syndica.backend.domain.repositories.UserApartmentRepository;
import com.syndica.backend.execptions.BusinessRuleException;

import jakarta.transaction.Transactional;


@Service
public class ApartmentService {
    private final UserApartmentRepository userApartmentRepository;

    public ApartmentService(
        UserApartmentRepository userApartmentRepository

    ){
        this.userApartmentRepository = userApartmentRepository;
    }

    public void listAllAlocations(){
        userApartmentRepository.findAll();
    }

    public void allocateUser(User user, Apartment apartment){
        if (userApartmentRepository.findByUserIdAndApartmentIdAndExitMomentIsNull(
            user.getId(), apartment.getId())
            .isPresent()
        ) {
            throw new BusinessRuleException("User already exists on apartment");
        }

        userApartmentRepository.save(
            UserApartment.builder()
                .user(user)
                .apartment(apartment)
                .enterMoment(Instant.now())
                .build()
        );
    }

    @Transactional
    public void unallocateUser(Long id) throws BusinessRuleException {
        UserApartment userApartment = userApartmentRepository.findById(id)
            .orElseThrow(() -> new BusinessRuleException("Vínculo não encontrado"));

        userApartment.setExitMoment(Instant.now());
    }
    
}