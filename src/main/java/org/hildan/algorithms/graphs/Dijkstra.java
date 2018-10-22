package org.hildan.algorithms.graphs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

class Dijkstra {

    static List<Node> shortestPath(Graph graph, Node source, Node target) {
        source.setDistance(0);

        Set<Node> closed = new HashSet<>();
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparing(Node::getDistance));

        open.add(source);

        while (open.size() > 0) {
            Node currentNode = open.poll();
            if (currentNode == target) {
                List<Node> pathToTarget = new ArrayList<>(currentNode.getShortestPath());
                pathToTarget.add(target);
                return pathToTarget;
            }
            for (Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!closed.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    open.add(adjacentNode);
                }
            }
            closed.add(currentNode);
        }
        // no path from source to target
        return null;
    }

    static Graph calculateAllShortestPathsFromSource(Graph graph, Node source) {
        source.setDistance(0);

        Set<Node> closed = new HashSet<>();
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparing(Node::getDistance));

        open.add(source);

        while (open.size() > 0) {
            Node currentNode = open.poll();
            for (Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!closed.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    open.add(adjacentNode);
                }
            }
            closed.add(currentNode);
        }
        return graph;
    }

    private static void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    static class Graph {

        private Set<Node> nodes = new HashSet<>();

        public void addNode(Node nodeA) {
            nodes.add(nodeA);
        }

        public Set<Node> getNodes() {
            return nodes;
        }

        public void setNodes(Set<Node> nodes) {
            this.nodes = nodes;
        }
    }

    static class Node {

        private final String id;

        private List<Node> shortestPath = new LinkedList<>();

        private Integer distance = Integer.MAX_VALUE;

        Map<Node, Integer> adjacentNodes = new HashMap<>();

        public Node(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void addEdge(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }

        List<Node> getShortestPath() {
            return shortestPath;
        }

        void setShortestPath(List<Node> shortestPath) {
            this.shortestPath = shortestPath;
        }

        Integer getDistance() {
            return distance;
        }

        void setDistance(Integer distance) {
            this.distance = distance;
        }

        Map<Node, Integer> getAdjacentNodes() {
            return adjacentNodes;
        }

        public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
            this.adjacentNodes = adjacentNodes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return Objects.equals(id, node.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "Node{" + "id='" + id + '\'' + ", distance=" + distance + '}';
        }
    }
}
