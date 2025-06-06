package com.abdulkhaleel.blockchain_voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
		"com.abdulkhaleel.blockchain_voting.election.model",
		"com.abdulkhaleel.blockchain_voting.user.model"
})
public class BlockchainVotingApplication {

	public static void main(String[] args) {

		SpringApplication.run(BlockchainVotingApplication.class, args);
	}

}
