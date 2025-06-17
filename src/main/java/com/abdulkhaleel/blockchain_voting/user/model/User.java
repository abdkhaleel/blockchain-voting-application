package com.abdulkhaleel.blockchain_voting.user.model;

import com.abdulkhaleel.blockchain_voting.election.model.Election;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"roles", "eligibleElections"})
@EqualsAndHashCode(exclude = {"roles", "eligibleElections"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private  String username;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @JsonIgnore

    @Column(nullable = false)
    private String password;

    private String firstname;
    private String lastname;

    @Column(nullable = false)
    private boolean isEnabled = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "eligibleVoters")
    @JsonIgnore
    private Set<Election> eligibleElections = new HashSet<>();
}
