package com.example.visualizer.model;

import java.util.List;

public record GridStep(
        List<GridNode> grid, // Flattened 2D grid
        String description,
        Integer activeLine
) {}