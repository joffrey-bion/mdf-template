package org.hildan.algorithms.graphs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStarGrid {

    private final boolean acceptDiagonal;

    private final int hCost;
    private final int vCost;
    private final int dCost;

    private final Node[][] grid;

    private PriorityQueue<Node> openList;
    private List<Node> closedList;

    public AStarGrid(Node[][] grid, int hCost, int vCost, int dCost, boolean acceptDiagonal) {
        this.hCost = hCost;
        this.vCost = vCost;
        this.dCost = dCost;
        this.grid = grid;
        this.acceptDiagonal = acceptDiagonal;
    }

    static Node[][] createEmptyGrid(int rows, int cols) {
        Node[][] grid = new Node[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Node node = new Node(i, j);
                grid[i][j] = node;
            }
        }
        return grid;
    }

    public List<Node> shortestPath(Node initialNode, Node finalNode) {
        initializeNodes(finalNode);
        this.openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        this.closedList = new ArrayList<>();
        openList.add(initialNode);
        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            closedList.add(currentNode);
            if (finalNode.equals(currentNode)) {
                return getPath(currentNode);
            }
            expandNeighbours(currentNode);
        }
        return new ArrayList<>();
    }

    private void initializeNodes(Node finalNode) {
        for (Node[] row : grid) {
            for (Node node : row) {
                node.setH(computeH(node, finalNode, acceptDiagonal));
                node.setG(0);
            }
        }
    }

    private static int computeH(Node n1, Node n2, boolean diagonalAccepted) {
        int hDist = Math.abs(n1.getRow() - n2.getRow());
        int vDist = Math.abs(n1.getCol() - n2.getCol());
        if (diagonalAccepted) {
            return Math.max(hDist, vDist);
        } else {
            return hDist + vDist;
        }
    }

    private static List<Node> getPath(Node currentNode) {
        List<Node> path = new ArrayList<>();
        path.add(currentNode);
        Node parent;
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    private void expandNeighbours(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int height = grid.length;
        int width = grid[0].length;
        if (row - 1 >= 0) {
            if (col - 1 >= 0) {
                expand(currentNode, col - 1, row - 1, dCost); // Comment this if diagonal movements are not allowed
            }
            if (col + 1 < width) {
                expand(currentNode, col + 1, row - 1, dCost); // Comment this if diagonal movements are not allowed
            }
            expand(currentNode, col, row - 1, vCost);
        }
        if (col - 1 >= 0) {
            expand(currentNode, col - 1, row, hCost);
        }
        if (col + 1 < width) {
            expand(currentNode, col + 1, row, hCost);
        }
        if (row + 1 < height) {
            if (col - 1 >= 0) {
                expand(currentNode, col - 1, row + 1, dCost); // Comment this line if diagonal movements are not allowed
            }
            if (col + 1 < width) {
                expand(currentNode, col + 1, row + 1, dCost); // Comment this line if diagonal movements are not allowed
            }
            expand(currentNode, col, row + 1, vCost);
        }
    }

    private void expand(Node currentNode, int col, int row, int localCost) {
        Node adjacentNode = grid[row][col];
        if (adjacentNode.isBlock() || closedList.contains(adjacentNode)) {
            return;
        }
        int newGCost = currentNode.getG() + localCost;
        if (!openList.contains(adjacentNode)) {
            adjacentNode.setBetterCost(currentNode, localCost);
            openList.add(adjacentNode);
        } else if (newGCost < adjacentNode.getG()) {
            adjacentNode.setBetterCost(currentNode, localCost);
            // Remove and Add the changed node, so that the PriorityQueue can sort again its
            // contents with the modified "finalCost" value of the modified node
            openList.remove(adjacentNode);
            openList.add(adjacentNode);
        }
    }

    static class Node {

        private int g;

        private int h;

        private int row;

        private int col;

        private boolean isBlock;

        private Node parent;

        public Node(int row, int col) {
            super();
            this.row = row;
            this.col = col;
        }

        public void setBetterCost(Node newParent, int stepCost) {
            int gCost = newParent.getG() + stepCost;
            setParent(newParent);
            setG(gCost);
        }

        public boolean checkBetterPath(Node currentNode, int cost) {
            int gCost = currentNode.getG() + cost;
            if (gCost < getG()) {
                setBetterCost(currentNode, cost);
                return true;
            }
            return false;
        }

        @Override
        public boolean equals(Object arg0) {
            Node other = (Node) arg0;
            return this.getRow() == other.getRow() && this.getCol() == other.getCol();
        }

        @Override
        public String toString() {
            return "Node [row=" + row + ", col=" + col + "]";
        }

        public int getF() {
            return g + h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public int getG() {
            return g;
        }

        public void setG(int g) {
            this.g = g;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public boolean isBlock() {
            return isBlock;
        }

        public void setBlock(boolean isBlock) {
            this.isBlock = isBlock;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }
    }
}
