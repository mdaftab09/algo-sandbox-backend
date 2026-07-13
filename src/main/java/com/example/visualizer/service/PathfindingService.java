package com.example.visualizer.service;

import com.example.visualizer.model.GridNode;
import com.example.visualizer.model.GridStep;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PathfindingService {

    private final SimpMessagingTemplate messagingTemplate;

    public PathfindingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void executeDijkstra(int rows, int cols, int[] start, int[] end) {
        boolean[][] visited = new boolean[rows][cols];
        boolean[][] walls = new boolean[rows][cols];

        // 1. GENERATE RANDOM WALLS (30% density)
        Random rand = new Random();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (rand.nextDouble() < 0.3 && !(r == start[0] && c == start[1]) && !(r == end[0] && c == end[1])) {
                    walls[r][c] = true;
                }
            }
        }

        int[][][] parent = new int[rows][cols][2];
        for(int r=0; r<rows; r++) {
            for(int c=0; c<cols; c++) {
                parent[r][c] = new int[]{-1, -1};
            }
        }

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        pq.add(new int[]{start[0], start[1], 0});
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        sendSnapshot(rows, cols, visited, new boolean[rows][cols], walls, start, end, "Starting Dijkstra's Search...", 1);

        while (!pq.isEmpty()) {
            if (Thread.currentThread().isInterrupted()) return;

            int[] current = pq.poll();
            int r = current[0];
            int c = current[1];
            int dist = current[2];

            if (visited[r][c]) continue;
            visited[r][c] = true;

            sendSnapshot(rows, cols, visited, new boolean[rows][cols], walls, start, end, "Searching...", 4);

            if (r == end[0] && c == end[1]) {
                // 2. TARGET FOUND! Animate the Breadcrumb Trail
                List<int[]> pathNodes = new ArrayList<>();
                int currR = end[0];
                int currC = end[1];

                while(currR != -1 && currC != -1) {
                    pathNodes.add(new int[]{currR, currC});
                    int nextR = parent[currR][currC][0];
                    int nextC = parent[currR][currC][1];
                    currR = nextR;
                    currC = nextC;
                }

                // Reverse it so the animation starts at the Green node and moves to the Red node
                Collections.reverse(pathNodes);
                boolean[][] finalPath = new boolean[rows][cols];

                for (int[] pNode : pathNodes) {
                    if (Thread.currentThread().isInterrupted()) return;
                    finalPath[pNode[0]][pNode[1]] = true;
                    // Send a snapshot for EACH step of the path to animate it!
                    sendSnapshot(rows, cols, visited, finalPath, walls, start, end, "Drawing Shortest Path...", 5);
                    try { Thread.sleep(40); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }

                sendSnapshot(rows, cols, visited, finalPath, walls, start, end, "Shortest Path Complete!", 5);
                return;
            }

            for (int[] dir : directions) {
                int newRow = r + dir[0];
                int newCol = c + dir[1];

                // 3. SKIP WALLS during the search
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && !visited[newRow][newCol] && !walls[newRow][newCol]) {
                    if (parent[newRow][newCol][0] == -1) {
                        parent[newRow][newCol] = new int[]{r, c};
                    }
                    pq.add(new int[]{newRow, newCol, dist + 1});
                }
            }
        }
    }

    private void sendSnapshot(int rows, int cols, boolean[][] visitedMatrix, boolean[][] pathMatrix, boolean[][] walls, int[] start, int[] end, String description, int activeLine) {
        List<GridNode> grid = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                boolean isStart = (r == start[0] && c == start[1]);
                boolean isEnd = (r == end[0] && c == end[1]);
                grid.add(new GridNode(r, c, walls[r][c], visitedMatrix[r][c], pathMatrix[r][c], isStart, isEnd));
            }
        }
        GridStep step = new GridStep(grid, description, activeLine);
        messagingTemplate.convertAndSend("/topic/algorithm-stream", step);
        try {
            Thread.sleep(25); // Faster search wave
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}