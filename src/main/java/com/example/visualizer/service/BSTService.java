package com.example.visualizer.service;

import com.example.visualizer.model.EdgeData;
import com.example.visualizer.model.NodeData;
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

    // Internal Node class to manage the tree structure
    static class TreeNode {
        int id;
        int value;
        TreeNode left, right;
        int x, y; // Screen coordinates (percentages)

        TreeNode(int id, int value, int x, int y) {
            this.id = id;
            this.value = value;
            this.x = x;
            this.y = y;
        }
    }

    public void executeBSTInsertion(List<Integer> values) {
        if (values == null || values.isEmpty()) return;

        TreeNode root = null;
        List<NodeData> currentNodes = new ArrayList<>();
        List<EdgeData> currentEdges = new ArrayList<>();
        int nodeIdCounter = 1;

        // Send initial empty state
        sendSnapshot(currentNodes, currentEdges, null, "Initializing BST...", 0);

        for (int value : values) {
            sendSnapshot(currentNodes, currentEdges, null, "Inserting value: " + value, 1);

            if (root == null) {
                // Root goes top center (X: 50%, Y: 10%)
                root = new TreeNode(nodeIdCounter++, value, 50, 22);
                currentNodes.add(new NodeData(root.id, root.value, root.y, root.x));
                sendSnapshot(currentNodes, currentEdges, root.id, "Placed root node: " + value, 2);
            } else {
                insertNode(root, value, nodeIdCounter++, currentNodes, currentEdges, 50, 22, 25);
            }
        }

        sendSnapshot(currentNodes, currentEdges, null, "BST Construction Complete!", 0);
    }

    private void insertNode(TreeNode current, int value, int newId, List<NodeData> nodes, List<EdgeData> edges, int currentX, int currentY, int horizontalGap) {
        sendSnapshot(nodes, edges, current.id, "Comparing " + value + " with " + current.value, 3);

        try {
            Thread.sleep(600); // Slow down the stream so the frontend can animate!
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (value < current.value) {
            sendSnapshot(nodes, edges, current.id, value + " is less than " + current.value + ". Going left.", 4);
            if (current.left == null) {
                // Calculate new position (shift left, move down)
                int newX = currentX - horizontalGap;
                int newY = currentY + 15;

                current.left = new TreeNode(newId, value, newX, newY);
                nodes.add(new NodeData(newId, value, newY, newX));
                edges.add(new EdgeData(current.id, newId));

                sendSnapshot(nodes, edges, newId, "Inserted " + value + " as left child.", 5);
            } else {
                insertNode(current.left, value, newId, nodes, edges, current.left.x, current.left.y, horizontalGap / 2);
            }
        } else {
            sendSnapshot(nodes, edges, current.id, value + " is greater than " + current.value + ". Going right.", 6);
            if (current.right == null) {
                // Calculate new position (shift right, move down)
                int newX = currentX + horizontalGap;
                int newY = currentY + 15;

                current.right = new TreeNode(newId, value, newX, newY);
                nodes.add(new NodeData(newId, value, newY, newX));
                edges.add(new EdgeData(current.id, newId));

                sendSnapshot(nodes, edges, newId, "Inserted " + value + " as right child.", 7);
            } else {
                insertNode(current.right, value, newId, nodes, edges, current.right.x, current.right.y, horizontalGap / 2);
            }
        }
    }

    private void sendSnapshot(List<NodeData> nodes, List<EdgeData> edges, Integer activeNodeId, String description, int activeLine) {
        // We create deep copies so the snapshot represents this exact moment in time
        TreeStep step = new TreeStep(
                new ArrayList<>(nodes),
                new ArrayList<>(edges),
                activeNodeId,
                description,
                activeLine
        );
        messagingTemplate.convertAndSend("/topic/algorithm-stream", step);

        try {
            Thread.sleep(800); // The WebSocket heartbeat
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}