package com.example.visualizer.model;

public record GridNode(int row, int col, boolean isWall, boolean isVisited, boolean isPath, boolean isStart, boolean isEnd) {}