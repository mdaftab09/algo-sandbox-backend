package com.example.visualizer.service;

import com.example.visualizer.model.AlgorithmStep;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BubbleSortService {

    public List<AlgorithmStep> executeBubbleSort(List<Integer> input) {
        List<AlgorithmStep> steps = new ArrayList<>();
        List<Integer> currentArray = new ArrayList<>(input);
        int n = currentArray.size();

        // Line 0 means no specific code line is active yet
        steps.add(new AlgorithmStep(new ArrayList<>(currentArray), List.of(), "Starting Bubble Sort", 0));

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Snapshot: Line 3 (Comparing)
                steps.add(new AlgorithmStep(
                        new ArrayList<>(currentArray),
                        List.of(j, j + 1),
                        "Comparing " + currentArray.get(j) + " and " + currentArray.get(j + 1),
                        3 // <-- ACTIVE LINE
                ));

                if (currentArray.get(j) > currentArray.get(j + 1)) {
                    Collections.swap(currentArray, j, j + 1);

                    // Snapshot: Line 4 (Swapping)
                    steps.add(new AlgorithmStep(
                            new ArrayList<>(currentArray),
                            List.of(j, j + 1),
                            "Swapped " + currentArray.get(j + 1) + " and " + currentArray.get(j),
                            4 // <-- ACTIVE LINE
                    ));
                }
            }
        }

        steps.add(new AlgorithmStep(new ArrayList<>(currentArray), List.of(), "Array is sorted!", 0));
        return steps;
    }
}