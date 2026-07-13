package com.example.visualizer.model;

import java.util.List;

public record AlgorithmStep(
        List<Integer> arrayState,
        List<Integer> activeIndices,
        String description,
        Integer activeLine // note for me: is used to track which line of code is running
) {}