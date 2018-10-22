package org.hildan.algorithms.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class AStar<T> {

    private final Graph<T> graph;

    public AStar(Graph<T> graph) {
        this.graph = graph;
    }

    /**
     * Implements the A-star algorithm and returns the path from source to destination
     *
     * @param source
     *         the source nodeid
     * @param destination
     *         the destination nodeid
     *
     * @return the path from source to destination
     */
    public List<T> aStar(T source, T destination) {
        /**
         * http://stackoverflow.com/questions/20344041/why-does-priority-queue-has-default-initial-capacity-of-11
         */
        final Queue<Node<T>> openQueue = new PriorityQueue<>(Comparator.comparing(Node::getF));

        Node<T> sourceNode = graph.getNodeData(source);
        sourceNode.setG(0);
        sourceNode.calcF(destination);
        openQueue.add(sourceNode);

        final Map<T, T> path = new HashMap<>();
        final Set<Node<T>> closedList = new HashSet<>();

        while (!openQueue.isEmpty()) {
            final Node<T> node = openQueue.poll();

            if (node.getNodeId().equals(destination)) {
                return path(path, destination);
            }

            closedList.add(node);

            for (Entry<Node<T>, Double> neighborEntry : graph.edgesFrom(node.getNodeId()).entrySet()) {
                Node<T> neighbor = neighborEntry.getKey();

                if (closedList.contains(neighbor)) {
                    continue;
                }

                double distanceBetweenTwoNodes = neighborEntry.getValue();
                double tentativeG = distanceBetweenTwoNodes + node.getG();

                if (tentativeG < neighbor.getG()) {
                    neighbor.setG(tentativeG);
                    neighbor.calcF(destination);

                    path.put(neighbor.getNodeId(), node.getNodeId());
                    if (!openQueue.contains(neighbor)) {
                        openQueue.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private List<T> path(Map<T, T> path, T destination) {
        assert path != null;
        assert destination != null;

        final List<T> pathList = new ArrayList<>();
        pathList.add(destination);
        while (path.containsKey(destination)) {
            destination = path.get(destination);
            pathList.add(destination);
        }
        Collections.reverse(pathList);
        return pathList;
    }

    static final class Node<T> {

        private final T nodeId;

        private final Map<T, Double> heuristic;

        private double g;  // g is distance from the source
        private double h;  // h is the heuristic of destination.
        private double f;  // f = g + h

        public Node(T nodeId, Map<T, Double> heuristic) {
            this.nodeId = nodeId;
            this.g = Double.MAX_VALUE;
            this.heuristic = heuristic;
        }

        public T getNodeId() {
            return nodeId;
        }

        public double getG() {
            return g;
        }

        public void setG(double g) {
            this.g = g;
        }

        public void calcF(T destination) {
            this.h = heuristic.get(destination);
            this.f = g + h;
        }

        public double getH() {
            return h;
        }

        public double getF() {
            return f;
        }
    }


    /**
     * The graph represents an undirected graph.
     */
    static final class Graph<T> implements Iterable<T> {
        /**
         * A map from the nodeId to outgoing edge. An outgoing edge is represented as a tuple of Node and the edge
         * length
         */
        private final Map<T, Map<Node<T>, Double>> graph;

        /**
         * A map of heuristic from a node to each other node in the graph.
         */
        private final Map<T, Map<T, Double>> heuristicMap;

        /**
         * A map between nodeId and nodedata.
         */
        private final Map<T, Node<T>> dataById;

        public Graph(Map<T, Map<T, Double>> heuristicMap) {
            if (heuristicMap == null) {
                throw new NullPointerException("The heuristic map should not be null");
            }
            this.graph = new HashMap<>();
            this.dataById = new HashMap<>();
            this.heuristicMap = heuristicMap;
        }

        /**
         * Adds a new node to the graph. Internally it creates the nodeData and populates the heuristic map concerning input
         * node into node data.
         *
         * @param nodeId
         *         the node to be added
         */
        public void addNode(T nodeId) {
            if (nodeId == null) {
                throw new NullPointerException("The node cannot be null");
            }
            if (!heuristicMap.containsKey(nodeId)) {
                throw new NoSuchElementException("This node is not a part of hueristic map");
            }

            graph.put(nodeId, new HashMap<>());
            dataById.put(nodeId, new Node<>(nodeId, heuristicMap.get(nodeId)));
        }

        /**
         * Adds an edge from source node to destination node. There can only be a single edge from source to node. Adding
         * additional edge would overwrite the value
         *
         * @param nodeIdFirst
         *         the first node to be in the edge
         * @param nodeIdSecond
         *         the second node to be second node in the edge
         * @param length
         *         the length of the edge.
         */
        public void addEdge(T nodeIdFirst, T nodeIdSecond, double length) {
            if (nodeIdFirst == null || nodeIdSecond == null) {
                throw new NullPointerException("The first nor second node can be null.");
            }

            if (!heuristicMap.containsKey(nodeIdFirst) || !heuristicMap.containsKey(nodeIdSecond)) {
                throw new NoSuchElementException("Source and Destination both should be part of the heuristic map");
            }
            if (!graph.containsKey(nodeIdFirst) || !graph.containsKey(nodeIdSecond)) {
                throw new NoSuchElementException("Source and Destination both should be part of the part of graph");
            }

            graph.get(nodeIdFirst).put(dataById.get(nodeIdSecond), length);
            graph.get(nodeIdSecond).put(dataById.get(nodeIdFirst), length);
        }

        /**
         * Returns immutable view of the edges
         *
         * @param nodeId
         *         the nodeId whose outgoing edge needs to be returned
         *
         * @return An immutable view of edges leaving that node
         */
        public Map<Node<T>, Double> edgesFrom(T nodeId) {
            if (nodeId == null) {
                throw new NullPointerException("The input node should not be null.");
            }
            if (!heuristicMap.containsKey(nodeId)) {
                throw new NoSuchElementException("This node is not a part of heuristic map");
            }
            if (!graph.containsKey(nodeId)) {
                throw new NoSuchElementException("The node should not be null.");
            }

            return Collections.unmodifiableMap(graph.get(nodeId));
        }

        /**
         * The nodedata corresponding to the current nodeId.
         *
         * @param nodeId
         *         the nodeId to be returned
         *
         * @return the nodeData from the
         */
        public Node<T> getNodeData(T nodeId) {
            if (nodeId == null) {
                throw new NullPointerException("The nodeid should not be null");
            }
            if (!dataById.containsKey(nodeId)) {
                throw new NoSuchElementException("The nodeId does not exist");
            }
            return dataById.get(nodeId);
        }

        /**
         * Returns an iterator that can traverse the nodes of the graph
         *
         * @return an Iterator.
         */
        @Override
        public Iterator<T> iterator() {
            return graph.keySet().iterator();
        }
    }
}
