package com.abdulkhaleel.blockchain_voting.election.model;

import com.abdulkhaleel.blockchain_voting.candidate.model.Candidate;
import com.abdulkhaleel.blockchain_voting.user.model.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "elections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"candidates", "eligibleVoters"})
@EqualsAndHashCode(exclude = {"candidates", "eligibleVoters"})
public class Election {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ElectionStatus status = ElectionStatus.PENDING;

    private boolean isPublic;
    private boolean allowRevote;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Candidate> candidates = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "election_voters", joinColumns = @JoinColumn(name = "election_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> eligibleVoters = new HashSet<>();

    @Column(unique = true)
    private String anchorHash;
}
