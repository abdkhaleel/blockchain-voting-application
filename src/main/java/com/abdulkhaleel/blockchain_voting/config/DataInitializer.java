package com.abdulkhaleel.blockchain_voting.config;

import com.abdulkhaleel.blockchain_voting.user.model.ERole;
import com.abdulkhaleel.blockchain_voting.user.model.Role;
import com.abdulkhaleel.blockchain_voting.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public void run(String... args){
        System.out.println("Executing data initializer...");

        if(roleRepository.findByName(ERole.ROLE_USER).isEmpty()){
            Role userRole = new Role();
            userRole.setName(ERole.ROLE_USER);
            roleRepository.save(userRole);
            System.out.println("Created ROLE_USER.");
        }

        if(roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()){
            Role userRole = new Role();
            userRole.setName(ERole.ROLE_ADMIN);
            roleRepository.save(userRole);
            System.out.println("Created ROLE_ADMIN.");
        }

        System.out.println("Data initializer finished");
    }
}
