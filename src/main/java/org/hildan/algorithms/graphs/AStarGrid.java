package org.hildan.algorithms.graphs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStarGrid {
    /** horizontal/vertical cost */
    private int hvCost;
    private int diagonalCost;
    private Node[][] grid;
    private PriorityQueue<Node> openList;
    private List<Node> closedList;
    private Node initialNode;
    private Node finalNode;

    public AStarGrid(int rows, int cols, Node initialNode, Node finalNode, int hvCost, int diagonalCost) {
        this.hvCost = hvCost;
        this.diagonalCost = diagonalCost;
        this.initialNode = initialNode;
        this.finalNode = finalNode;
        this.grid = new Node[rows][cols];
        this.openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        setNodes();
        this.closedList = new ArrayList<>();
    }

    private void setNodes() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Node node = new Node(i, j);
                node.calculateHeuristic(finalNode);
                this.grid[i][j] = node;
            }
        }
    }

    public void setBlocks(int[][] blocksArray) {
        for (int i = 0; i < blocksArray.length; i++) {
            int row = blocksArray[i][0];
            int col = blocksArray[i][1];
            setBlock(row, col);
        }
    }

    public List<Node> findPath() {
        openList.add(initialNode);
        while (!isEmpty(openList)) {
            Node currentNode = openList.poll();
            closedList.add(currentNode);
            if (isFinalNode(currentNode)) {
                return getPath(currentNode);
            } else {
                addAdjacentNodes(currentNode);
            }
        }
        return new ArrayList<>();
    }

    private List<Node> getPath(Node currentNode) {
        List<Node> path = new ArrayList<>();
        path.add(currentNode);
        Node parent;
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    private void addAdjacentNodes(Node currentNode) {
        addAdjacentUpperRow(currentNode);
        addAdjacentMiddleRow(currentNode);
        addAdjacentLowerRow(currentNode);
    }

    private void addAdjacentLowerRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int lowerRow = row + 1;
        if (lowerRow < grid.length) {
            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, lowerRow, diagonalCost); // Comment this line if diagonal movements are not allowed
            }
            if (col + 1 < grid[0].length) {
                checkNode(currentNode, col + 1, lowerRow, diagonalCost); // Comment this line if diagonal movements are not allowed
            }
            checkNode(currentNode, col, lowerRow, hvCost);
        }
    }

    private void addAdjacentMiddleRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int middleRow = row;
        if (col - 1 >= 0) {
            checkNode(currentNode, col - 1, middleRow, hvCost);
        }
        if (col + 1 < grid[0].length) {
            checkNode(currentNode, col + 1, middleRow, hvCost);
        }
    }

    private void addAdjacentUpperRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int upperRow = row - 1;
        if (upperRow >= 0) {
            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, upperRow, diagonalCost); // Comment this if diagonal movements are not allowed
            }
            if (col + 1 < grid[0].length) {
                checkNode(currentNode, col + 1, upperRow, diagonalCost); // Comment this if diagonal movements are not allowed
            }
            checkNode(currentNode, col, upperRow, hvCost);
        }
    }

    private void checkNode(Node currentNode, int col, int row, int cost) {
        Node adjacentNode = grid[row][col];
        if (!adjacentNode.isBlock() && !closedList.contains(adjacentNode)) {
            if (!openList.contains(adjacentNode)) {
                adjacentNode.setNodeData(currentNode, cost);
                openList.add(adjacentNode);
            } else {
                boolean changed = adjacentNode.checkBetterPath(currentNode, cost);
                if (changed) {
                    // Remove and Add the changed node, so that the PriorityQueue can sort again its
                    // contents with the modified "finalCost" value of the modified node
                    openList.remove(adjacentNode);
                    openList.add(adjacentNode);
                }
            }
        }
    }

    private boolean isFinalNode(Node currentNode) {
        return currentNode.equals(finalNode);
    }

    private boolean isEmpty(PriorityQueue<Node> openList) {
        return openList.size() == 0;
    }

    private void setBlock(int row, int col) {
        this.grid[row][col].setBlock(true);
    }

    class Node {

        private int g;
        private int f;
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

        public void calculateHeuristic(Node finalNode) {
            this.h = Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getCol() - getCol());
        }

        public void setNodeData(Node currentNode, int cost) {
            int gCost = currentNode.getG() + cost;
            setParent(currentNode);
            setG(gCost);
            calculateFinalCost();
        }

        public boolean checkBetterPath(Node currentNode, int cost) {
            int gCost = currentNode.getG() + cost;
            if (gCost < getG()) {
                setNodeData(currentNode, cost);
                return true;
            }
            return false;
        }

        private void calculateFinalCost() {
            int finalCost = getG() + getH();
            setF(finalCost);
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

        public int getH() {
            return h;
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

        public int getF() {
            return f;
        }

        public void setF(int f) {
            this.f = f;
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
