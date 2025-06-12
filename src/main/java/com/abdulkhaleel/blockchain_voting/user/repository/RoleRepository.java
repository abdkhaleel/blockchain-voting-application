package com.abdulkhaleel.blockchain_voting.user.repository;

import com.abdulkhaleel.blockchain_voting.user.model.ERole;
import com.abdulkhaleel.blockchain_voting.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
    @Query(value = "SELECT r.* FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = :userId", nativeQuery = true)
    Set<Role> findRolesByUserId(@Param("userId") Long userId);
}
