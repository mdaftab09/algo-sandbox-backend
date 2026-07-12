package com.example.visualizer.service;

import com.example.visualizer.model.TreeStep;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BSTService {

    private final SimpMessagingTemplate messagingTemplate;

    public BSTService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Simplified simulation of streaming BST insertion
    public void executeBSTInsertion(List<Integer> values) {
        List<TreeStep> history = new ArrayList<>();

        // In a full implementation, you'd build the tree node-by-node
        // and stream each step. Here we stream a simulation placeholder
        // to verify the WebSocket pipeline works.
        for(int i = 0; i < values.size(); i++) {
            messagingTemplate.convertAndSend(
                    "/topic/algorithm-stream",
                    "Inserting value: " + values.get(i)
            );

            try {
                Thread.sleep(500); // Simulate processing time to prove streaming works
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        messagingTemplate.convertAndSend("/topic/algorithm-stream", "BST Complete!");
    }
}