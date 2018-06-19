package aima.core.search.framework.qsearch;

import aima.core.agent.State;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.Node;
import aima.core.search.framework.NodeExpander;
import aima.core.search.framework.problem.Problem;
import aima.core.util.Tasks;
import edu.uci.ics.jung.graph.DelegateForest;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import edu.uci.ics.jung.graph.Forest;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * Base class for queue-based search implementations, especially for
 * {@link TreeSearch}, {@link GraphSearch}, and {@link BidirectionalSearch}. It
 * provides a template method for controlling search execution and defines
 * primitive methods encapsulating frontier access. Tree search implementations
 * will implement frontier access straight-forward. Graph search implementations
 * will add node filtering mechanisms to avoid that nodes of already explored
 * states are selected for expansion.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state
 * space
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public abstract class QueueSearch<S, A> {

    public static final String METRIC_NODES_EXPANDED = "nodesExpanded";
    public static final String METRIC_QUEUE_SIZE = "queueSize";
    public static final String METRIC_MAX_QUEUE_SIZE = "maxQueueSize";
    public static final String METRIC_PATH_COST = "pathCost";

    final protected NodeExpander<S, A> nodeExpander;
    protected Queue<Node<S, A>> frontier;
    protected boolean earlyGoalTest = false;
    protected Metrics metrics = new Metrics();

    /**
     * Stores the provided node expander and adds a node listener to it.
     */
    protected QueueSearch(NodeExpander<S, A> nodeExpander) {
        this.nodeExpander = nodeExpander;
        nodeExpander.addNodeListener((node) -> metrics.incrementInt(METRIC_NODES_EXPANDED));
    }

    /**
     * Receives a problem and a queue implementing the search strategy and
     * computes a node referencing a goal state, if such a state was found. This
     * template method provides a base for tree and graph search
     * implementations. It can be customized by overriding some primitive
     * operations, especially {@link #addToFrontier(Node)},
     * {@link #removeFromFrontier()}, and {@link #isFrontierEmpty()}.
     *
     * @param problem the search problem
     * @param frontier the data structure for nodes that are waiting to be
     * expanded
     *
     * @return a node referencing a goal state, if the goal was found, otherwise
     * empty;
     *
     */
    public Optional<Node<S, A>> findNode(Problem<S, A> problem, Queue<Node<S, A>> frontier) {
        this.frontier = frontier;
        clearMetrics();
        // initialize the frontier using the initial state of the problem
        Node<S, A> root = nodeExpander.createRootNode(problem.getInitialState());
        addToFrontier(root);
        if (earlyGoalTest && problem.testSolution(root)) {
            return getSolution(root);
        }

        int numberOfLoop = 1;
        while (!isFrontierEmpty() && !Tasks.currIsCancelled()) {
            System.out.println("numberOfLoop = " + numberOfLoop++);
            // choose a leaf node and remove it from the frontier
            //PrintTree(root, 0);
//            System.out.println(root.getAction());
            Node<S, A> nodeToExpand = removeFromFrontier();
            //System.out.println(nodeToExpand.getState().toString());

            // only need to check the nodeToExpand if have not already
            // checked before adding to the frontier
            if (!earlyGoalTest && problem.testSolution(nodeToExpand)) {
                // if the node contains a goal state then return the
                // corresponding solution
                saveTree(root, "");
                return getSolution(nodeToExpand);
            }
            // expand the chosen node, adding the resulting nodes to the
            // frontier
            nodeExpander.expand(nodeToExpand, problem);
            for (Node<S, A> successor : nodeToExpand.successors) {

                addToFrontier(successor);
                if (earlyGoalTest && problem.testSolution(successor)) {
                    System.out.println("Result:");
                    //PrintTree(root, 0);
                    saveTree(root, "");
                    return getSolution(successor);
                }
            }
        }
        // if the frontier is empty then return failure
        return Optional.empty();
    }

    public void PrintTree(Node<S, A> node, int level) {
        String print = "";
        for (int i = 0; i < level; i++) {
            print = print + "	";
        }
        if (node.getAction() != null) {
            System.out.println(print + node.getAction().toString());
        } else {
            System.out.println(print + "Initial State");
        }
        if (node.successors != null) {
            for (Node<S, A> successor : node.successors) {
                PrintTree(successor, level + 1);
            }
        }
    }
//    ####################################################################################
    public Forest<String, Integer> graph = new DelegateForest<String, Integer>();
    public int index = 0;

    public void saveTree(Node<S, A> node, String level) {
        if (node.getAction() != null) {
            graph.addVertex(reduceActionName(node, level));
            if (level.length() < 2) {
                graph.addEdge(index++, "INIT", reduceActionName(node, level));
            } else {
                graph.addEdge(index++, reduceActionName(node.getParent(), level.substring(0, level.length() - 2)), reduceActionName(node, level));
            }
        } else {
            graph.addVertex("INIT");
        }
        if (node.successors != null) {
            int levelIndex = 0;
            for (Node<S, A> successor : node.successors) {
                int currentIndex = levelIndex++;
                if (currentIndex < 10) saveTree(successor, level + "0" + currentIndex);
                else saveTree(successor, level + currentIndex);
            }
        }
        
        
        
        if (node.getAction() == null) {
            try {
                FileOutputStream fos = new FileOutputStream("tree.ser");
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(graph);
                out.close();
                fos.close();
//                System.out.println("Write to file " + graph.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String reduceActionName(Node<S, A> node, String level) {
        if (level.equals("")) return "INIT";
        return node.getState().toString() + "    " + node.getAction().toString().split("\\[")[1].split("=")[1].split("\\]")[0] + "    " + level;
    }
//###############################################################################################
    /**
     * Primitive operation which inserts the node at the tail of the frontier.
     */
    protected abstract void addToFrontier(Node<S, A> node);

    /**
     * Primitive operation which removes and returns the node at the head of the
     * frontier.
     *
     * @return the node at the head of the frontier.
     */
    protected abstract Node<S, A> removeFromFrontier();

    /**
     * Primitive operation which checks whether the frontier contains not yet
     * expanded nodes.
     */
    protected abstract boolean isFrontierEmpty();

    /**
     * Enables optimization for FIFO queue based search, especially breadth
     * first search.
     */
    public void setEarlyGoalTest(boolean b) {
        earlyGoalTest = b;
    }

    public NodeExpander<S, A> getNodeExpander() {
        return nodeExpander;
    }

    /**
     * Returns all the search metrics.
     */
    public Metrics getMetrics() {
        return metrics;
    }

    /**
     * Sets all metrics to zero.
     */
    protected void clearMetrics() {
        metrics.set(METRIC_NODES_EXPANDED, 0);
        metrics.set(METRIC_QUEUE_SIZE, 0);
        metrics.set(METRIC_MAX_QUEUE_SIZE, 0);
        metrics.set(METRIC_PATH_COST, 0);
    }

    protected void updateMetrics(int queueSize) {
        metrics.set(METRIC_QUEUE_SIZE, queueSize);
        int maxQSize = metrics.getInt(METRIC_MAX_QUEUE_SIZE);
        if (queueSize > maxQSize) {
            metrics.set(METRIC_MAX_QUEUE_SIZE, queueSize);
        }
    }

    private Optional<Node<S, A>> getSolution(Node<S, A> node) {
        metrics.set(METRIC_PATH_COST, node.getPathCost());
        return Optional.of(node);
    }
}
