package com.abdulkhaleel.blockchain_voting.candidate.model;

import com.abdulkhaleel.blockchain_voting.election.model.Election;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*; // Make sure this import is present
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String party;

    private String manifesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id", nullable = false)
    @JsonBackReference
    private Election election;
}