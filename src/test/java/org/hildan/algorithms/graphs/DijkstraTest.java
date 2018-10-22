package org.hildan.algorithms.graphs;

import java.util.Arrays;
import java.util.List;

import org.hildan.algorithms.graphs.Dijkstra.Graph;
import org.hildan.algorithms.graphs.Dijkstra.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DijkstraTest {

    @Test
    void shortestPath() {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");

        nodeA.addEdge(nodeB, 10);
        nodeA.addEdge(nodeC, 15);

        nodeB.addEdge(nodeD, 12);
        nodeB.addEdge(nodeF, 15);

        nodeC.addEdge(nodeE, 10);

        nodeD.addEdge(nodeE, 2);
        nodeD.addEdge(nodeF, 1);

        nodeF.addEdge(nodeE, 5);

        Graph graph = new Graph();

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);

        List<Node> path1 = Dijkstra.shortestPath(graph, nodeA, nodeD);
        List<Node> expected1 = Arrays.asList(nodeA, nodeB, nodeD);
        assertEquals(expected1, path1);

        List<Node> path2 = Dijkstra.shortestPath(graph, nodeA, nodeF);
        List<Node> expected2 = Arrays.asList(nodeA, nodeB, nodeD, nodeF);
        assertEquals(expected2, path2);
    }
}
