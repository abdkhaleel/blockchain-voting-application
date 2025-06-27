package com.abdulkhaleel.blockchain_voting.blockchain.controller;

import com.abdulkhaleel.blockchain_voting.blockchain.model.Block;
import com.abdulkhaleel.blockchain_voting.blockchain.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/blockchain")
@CrossOrigin(origins = "http://localhost:8081")
@RequiredArgsConstructor
public class BlockchainController {

    private final BlockchainService blockchainService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Block>> getFullChain() {
        return ResponseEntity.ok(blockchainService.getChain());
    }
}