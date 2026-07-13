package com.example.visualizer.service;

import com.example.visualizer.model.AlgorithmStep;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QuickSortService {

    public List<AlgorithmStep> executeQuickSort(List<Integer> input) {
        List<AlgorithmStep> steps = new ArrayList<>();
        List<Integer> arr = new ArrayList<>(input);

        steps.add(new AlgorithmStep(new ArrayList<>(arr), List.of(), "Starting Quick Sort", 0));
        quickSort(arr, 0, arr.size() - 1, steps);
        steps.add(new AlgorithmStep(new ArrayList<>(arr), List.of(), "Array is sorted!", 0));

        return steps;
    }

    private void quickSort(List<Integer> arr, int low, int high, List<AlgorithmStep> steps) {
        steps.add(new AlgorithmStep(new ArrayList<>(arr), List.of(low, high), "Checking bounds", 1));

        if (low < high) {
            int pivotIndex = partition(arr, low, high, steps);

            steps.add(new AlgorithmStep(new ArrayList<>(arr), List.of(), "Sorting left partition", 3));
            quickSort(arr, low, pivotIndex - 1, steps);

            steps.add(new AlgorithmStep(new ArrayList<>(arr), List.of(), "Sorting right partition", 4));
            quickSort(arr, pivotIndex + 1, high, steps);
        }
    }

    private int partition(List<Integer> arr, int low, int high, List<AlgorithmStep> steps) {
        int pivot = arr.get(high);
        steps.add(new AlgorithmStep(new ArrayList<>(arr), List.of(high), "Selected pivot: " + pivot, 2));

        int i = (low - 1);
        for (int j = low; j < high; j++) {
            steps.add(new AlgorithmStep(new ArrayList<>(arr), List.of(j, high), "Comparing with pivot", 2));
            if (arr.get(j) < pivot) {
                i++;
                Collections.swap(arr, i, j);
                steps.add(new AlgorithmStep(new ArrayList<>(arr), List.of(i, j), "Swapped elements", 2));
            }
        }

        Collections.swap(arr, i + 1, high);
        steps.add(new AlgorithmStep(new ArrayList<>(arr), List.of(i + 1, high), "Placed pivot in correct spot", 2));

        return i + 1;
    }
}