package com.example.visualizer.controller;

import com.example.visualizer.model.AlgorithmStep;
import com.example.visualizer.service.BubbleSortService;
import com.example.visualizer.service.QuickSortService;
import com.example.visualizer.service.BSTService;
import com.example.visualizer.service.PathfindingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/visualize")
@CrossOrigin(origins = "http://localhost:5173")
public class AlgorithmController {

    private final BubbleSortService bubbleSortService;
    private final QuickSortService quickSortService;
    private final BSTService bstService;
    private final PathfindingService pathfindingService;

    // Tracker to prevent overlapping WebSocket streams!
    private Thread activeStreamThread;

    public AlgorithmController(BubbleSortService bubbleSortService, QuickSortService quickSortService, BSTService bstService, PathfindingService pathfindingService) {
        this.bubbleSortService = bubbleSortService;
        this.quickSortService = quickSortService;
        this.bstService = bstService;
        this.pathfindingService = pathfindingService;
    }

    @PostMapping("/sort/bubble")
    public List<AlgorithmStep> visualizeBubbleSort(@RequestBody List<Integer> array) {
        return bubbleSortService.executeBubbleSort(array);
    }

    @PostMapping("/sort/quick")
    public List<AlgorithmStep> visualizeQuickSort(@RequestBody List<Integer> array) {
        return quickSortService.executeQuickSort(array);
    }

    @PostMapping("/tree/bst")
    public void streamBSTInsertion(@RequestBody List<Integer> array) {
        if (activeStreamThread != null) activeStreamThread.interrupt(); // Kill old thread
        activeStreamThread = new Thread(() -> bstService.executeBSTInsertion(array));
        activeStreamThread.start();
    }

    @PostMapping("/graph/dijkstra")
    public void streamDijkstra() {
        if (activeStreamThread != null) activeStreamThread.interrupt(); // Kill old thread

        java.util.Random rand = new java.util.Random();
        int[] start = {rand.nextInt(10), rand.nextInt(4)};
        int[] end = {rand.nextInt(10), 16 + rand.nextInt(4)};

        activeStreamThread = new Thread(() -> pathfindingService.executeDijkstra(10, 20, start, end));
        activeStreamThread.start();
    }
}