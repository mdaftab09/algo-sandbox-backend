package com.example.visualizer.controller;

import com.example.visualizer.model.AlgorithmStep;
import com.example.visualizer.service.BubbleSortService;
import com.example.visualizer.service.QuickSortService;
import com.example.visualizer.service.BSTService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/visualize")
@CrossOrigin(origins = "http://localhost:5173")
public class AlgorithmController {

    // 1. Declare all three services
    private final BubbleSortService bubbleSortService;
    private final QuickSortService quickSortService;
    private final BSTService bstService;

    // 2. Inject all three into the constructor
    public AlgorithmController(BubbleSortService bubbleSortService, QuickSortService quickSortService, BSTService bstService) {
        this.bubbleSortService = bubbleSortService;
        this.quickSortService = quickSortService;
        this.bstService = bstService;
    }

    // 3. Endpoint 1: Bubble Sort
    @PostMapping("/sort/bubble")
    public List<AlgorithmStep> visualizeBubbleSort(@RequestBody List<Integer> array) {
        return bubbleSortService.executeBubbleSort(array);
    }

    // 4. Endpoint 2: Quick Sort
    @PostMapping("/sort/quick")
    public List<AlgorithmStep> visualizeQuickSort(@RequestBody List<Integer> array) {
        return quickSortService.executeQuickSort(array);
    }

    // 5. Endpoint 3: The New BST WebSocket Trigger
    @PostMapping("/tree/bst")
    public void streamBSTInsertion(@RequestBody List<Integer> array) {
        new Thread(() -> bstService.executeBSTInsertion(array)).start();
    }
}