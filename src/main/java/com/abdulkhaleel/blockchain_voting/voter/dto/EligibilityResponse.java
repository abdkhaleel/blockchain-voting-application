package com.abdulkhaleel.blockchain_voting.voter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityResponse {
    private boolean isEligible;
}
