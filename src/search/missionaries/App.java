package search.missionaries;

import java.util.List;
import java.util.Properties;

import aima.core.agent.Action;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.SearchForActions;
import aima.core.search.agent.SearchAgent;

import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.framework.qsearch.BidirectionalSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.search.uninformed.UniformCostSearch;


public class App {
    public static void main(String args[]) {
        //BFS();
        //UCS();
        DLS(11);
        //IDS();
        //DFS();
    }

    private static void BFS() {
        try {
            System.out.println("\n BFS -->");
            Problem<State, Action> problem = Function.createProblem();
            SearchForActions<State, Action> search = new BreadthFirstSearch<>(new TreeSearch<>());
            System.out.println("\n 3. Do search");
            SearchAgent<State, Action> agent = new SearchAgent<>(problem, search);
            System.out.println("\n 4. Print log");
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private static void DFS() {
        try {
            System.out.println("\n DFS -->");
            Problem<State, Action> problem = Function.createProblem();
            SearchForActions<State, Action> search = new DepthFirstSearch<>(new TreeSearch<>());
            SearchAgent<State, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void UCS() {
        try {
            System.out.println("\n UniformCostSearch -->");
            Problem<State, Action> problem = Function.createProblem();
            SearchForActions<State, Action> search = new UniformCostSearch<>(new TreeSearch<>());
            SearchAgent<State, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    private static void DLS(int depth) {
        try {
            System.out.println("\n DepthLimitedSearch -->");
            Problem<State, Action> problem = Function.createProblem();
            SearchForActions<State, Action> search = new DepthLimitedSearch<>(depth);
            SearchAgent<State, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*private static void IDS() {
        try {
            System.out.println("\n IterativeDeepeningSearch -->");
            Problem<State, Action> problem = Function.createProblem();
            SearchForActions<State, Action> search = new IterativeDeepeningSearch<>();
            SearchAgent<State, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    private static void printInstrumentation(Properties properties) {
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List<Action> actions) {
        actions.forEach(System.out::println);
    }
}
