package com.abdulkhaleel.blockchain_voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.abdulkhaleel.blockchain_voting")
@EnableJpaRepositories("com.abdulkhaleel.blockchain_voting")

public class BlockchainVotingApplication {

	public static void main(String[] args) {

		SpringApplication.run(BlockchainVotingApplication.class, args);
	}

}
