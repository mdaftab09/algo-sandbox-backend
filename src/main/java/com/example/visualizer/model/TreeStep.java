package com.example.visualizer.model;

import java.util.List;

public record TreeStep(
        List<NodeData> nodes,
        List<EdgeData> edges,
        Integer activeNodeId,
        String description,
        Integer activeLine
) {}