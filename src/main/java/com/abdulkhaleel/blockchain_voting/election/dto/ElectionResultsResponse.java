package com.abdulkhaleel.blockchain_voting.election.dto;

import com.abdulkhaleel.blockchain_voting.election.model.ElectionStatus;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectionResultsResponse {
    private Long electionId;
    private String name;
    private ElectionStatus status;
    private LocalDateTime startTime;
    private LocalDate endTime;
    private List<CandidateResultDto> results;
}
