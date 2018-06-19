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
import com.google.common.base.Functions;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class App extends JApplet {
    static Forest<String, Integer> graph;
    VisualizationViewer<String, Integer> vv;

    String root;

    TreeLayout<String, Integer> treeLayout;
    public static void main(String args[]) {
        //BFS();
        //UCS();
        //DFS();
        //DLS(11);
        //IDS();
        GreedyBestFirstSearchMisplacedTileHeuristic();
        //GreedyBestFirstSearchManhattanHeuristic();
        //AStarSearchMisplacedTileHeuristic();
        //AStarSearchManhattanHeuristic();
        readObj();
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        content.add(new App());
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void readObj() {
        try {
            FileInputStream fis = new FileInputStream("tree.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            graph = (Forest<String, Integer>) in.readObject();
            in.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(graph.toString());
    }
    
    public App() throws HeadlessException {
        treeLayout = new TreeLayout<String, Integer>(graph);
        vv = new VisualizationViewer<String, Integer>(treeLayout, new Dimension(600, 600));
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(graph));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        vv.setVertexToolTipTransformer(new ToStringLabeller());
        vv.getRenderContext().setArrowFillPaintTransformer(Functions.<Paint>constant(Color.lightGray));

        Container content = getContentPane();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);

        final DefaultModalGraphMouse<String, Integer> graphMouse
                = new DefaultModalGraphMouse<>();
        vv.setGraphMouse(graphMouse);

        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1 / 1.1f, vv.getCenter());
            }
        });

        JPanel scaleGrid = new JPanel(new GridLayout(1, 0));
        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));

        JPanel controls = new JPanel();
        scaleGrid.add(plus);
        scaleGrid.add(minus);
        controls.add(scaleGrid);

        content.add(controls, BorderLayout.SOUTH);
    }
    
    /*private static void BFS() {
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

    private static void DFS() {
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
    private static void IDS() {
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
    }
        private static void GreedyBestFirstSearchMisplacedTileHeuristic() {
        try {
            System.out.println("\nApp GreedyBestFirstSearch(MisplacedTileHeuristic) -->");
            Problem<State, Action> problem = Function.createProblem();
            SearchForActions<State, Action> search = new GreedyBestFirstSearch<>(new GraphSearch<>(),
                    Function.createMisplacedTileHeuristicFunction());
            SearchAgent<State, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void GreedyBestFirstSearchManhattanHeuristic() {
        try {
            System.out.println("\nApp GreedyBestFirstSearch(ManhattanHeuristic) -->");
            Problem<State, Action> problem = Function.createProblem();
            SearchForActions<State, Action> search = new GreedyBestFirstSearch<>(new GraphSearch<>(),
                    Function.createManhattanHeuristicFunction());
            SearchAgent<State, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void AStarSearchMisplacedTileHeuristic() {
        try {
            System.out.println("\nApp AStarSearch(MisplacedTileHeuristic) -->");
            Problem<State, Action> problem = Function.createProblem();
            SearchForActions<State, Action> search = new AStarSearch<>(new GraphSearch<>(),
                    Function.createMisplacedTileHeuristicFunction());
            SearchAgent<State, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void AStarSearchManhattanHeuristic() {
        try {
            System.out.println("\nApp AStarSearch(ManhattanHeuristic) -->");
            Problem<State, Action> problem = Function.createProblem();
            SearchForActions<State, Action> search = new AStarSearch<>(new GraphSearch<>(),
                    Function.createManhattanHeuristicFunction());
            SearchAgent<State, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
