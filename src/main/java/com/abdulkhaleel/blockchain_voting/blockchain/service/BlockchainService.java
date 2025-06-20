package com.abdulkhaleel.blockchain_voting.blockchain.service;

import com.abdulkhaleel.blockchain_voting.blockchain.model.Block;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlockchainService {

    private final List<Block> chain = new ArrayList<>();

    @PostConstruct
    private void init(){
        Block genesisBlock = new Block(0, "Genesis Block", "0");
        chain.add(genesisBlock);
    }

    public List<Block> getChain(){
        return new ArrayList<>(chain);
    }

    public Block getLatestBlock(){
        return chain.get(chain.size() - 1);
    }

    // In BlockchainService.java

    public void addBlock(String data) {
        // Get the last block in the chain to link the new one
        Block previousBlock = getLatestBlock();

        // The new block's index is one greater than the previous
        int newIndex = previousBlock.getIndex() + 1;

        // Create the new block, passing its index, data, and the HASH of the previous block
        Block newBlock = new Block(newIndex, data, previousBlock.getHash());

        // Perform a validation check before adding it to the chain
        if (isNewBlockValid(newBlock, previousBlock)) {
            chain.add(newBlock);
        } else {
            // If validation fails, throw a clear exception. This is what you are seeing.
            throw new IllegalStateException("Attempted to add an invalid block to the chain.");
        }
    }

    private boolean isNewBlockValid(Block newBlock, Block previousBlock) {
        if (previousBlock.getIndex() + 1 != newBlock.getIndex()) {
            return false; // Index is incorrect
        }
        if (!previousBlock.getHash().equals(newBlock.getPreviousHash())) {
            return false; // Previous hash does not match
        }
        if (!newBlock.getHash().equals(newBlock.calculateHash())) {
            return false; // Hash of the new block is invalid
        }
        return true;
    }
}
