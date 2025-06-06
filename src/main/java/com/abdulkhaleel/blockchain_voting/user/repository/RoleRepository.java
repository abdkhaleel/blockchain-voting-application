package com.abdulkhaleel.blockchain_voting.user.repository;

import com.abdulkhaleel.blockchain_voting.user.model.ERole;
import com.abdulkhaleel.blockchain_voting.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
