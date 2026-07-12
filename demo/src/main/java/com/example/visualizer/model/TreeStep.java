package com.example.visualizer.model;

import java.util.List;

// Represents the state of the tree at any given moment
public record TreeStep(
        List<NodeData> nodes, // Array of flattened tree nodes (id, value, x, y)
        List<EdgeData> edges, // Array of connections (fromId, toId)
        Integer activeNodeId, // The node currently being compared/moved
        String description,
        Integer activeLine
) {}

record NodeData(int id, int value, int level, int position) {}
record EdgeData(int fromId, int toId) {}