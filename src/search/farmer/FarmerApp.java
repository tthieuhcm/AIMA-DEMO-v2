package search.farmer;

import java.util.List;
import java.util.Properties;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.SearchForActions;
import aima.core.search.agent.SearchAgent;
import aima.core.search.framework.Node;

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
import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import java.io.FileInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class FarmerApp extends JApplet {

    static Forest<String, Integer> graph;
    VisualizationViewer<String, Integer> vv;

    String root;

    TreeLayout<String, Integer> treeLayout;

    public static void main(String args[]) {
        FarmerWithBreadthFirstSearch();
//        FarmerWithUniformCostSearch();
//        FarmerWithDepthLimitedSearch(8);
//        FarmerWithIterativeDeepeningSearch();
//        FarmerWithGreedyBestFirstSearchMisplacedTileHeuristic();
//       FarmerWithGreedyBestFirstSearchManhattanHeuristic();
//        FarmerWithAStarSearchMisplacedTileHeuristic();
//        FarmerWithAStarSearchManhattanHeuristic();
//        FarmerWithDepthFirstSearch();

        readObj();
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        content.add(new FarmerApp());
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


    public FarmerApp() throws HeadlessException {
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

    private static void FarmerWithBreadthFirstSearch() {
        try {
            System.out.println("\nFarmerAppDemo BFS -->");
            Problem<FarmerState, Action> problem = FarmerFunction.createProblem();
            SearchForActions<FarmerState, Action> search = new BreadthFirstSearch<>(new GraphSearch<>());
            SearchAgent<FarmerState, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void FarmerWithDepthFirstSearch() {
        try {
            System.out.println("\nFarmerAppDemo DFS -->");
            Problem<FarmerState, Action> problem = FarmerFunction.createProblem();
            SearchForActions<FarmerState, Action> search = new DepthFirstSearch<>(new GraphSearch<>());
            SearchAgent<FarmerState, Action> agent = new SearchAgent<>(problem, search);
//			printActions(agent.getActions());
//			printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void FarmerWithUniformCostSearch() {
        try {
            System.out.println("\nFarmerAppDemo UniformCostSearch -->");
            Problem<FarmerState, Action> problem = FarmerFunction.createProblem();
            SearchForActions<FarmerState, Action> search = new UniformCostSearch<>(new GraphSearch<>());
            SearchAgent<FarmerState, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void FarmerWithDepthLimitedSearch(int depth) {
        try {
            System.out.println("\nFarmerAppDemo DepthLimitedSearch -->");
            Problem<FarmerState, Action> problem = FarmerFunction.createProblem();
            SearchForActions<FarmerState, Action> search = new DepthLimitedSearch<>(depth);
            SearchAgent<FarmerState, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void FarmerWithIterativeDeepeningSearch() {
        try {
            System.out.println("\nFarmerAppDemo IterativeDeepeningSearch -->");
            Problem<FarmerState, Action> problem = FarmerFunction.createProblem();
            SearchForActions<FarmerState, Action> search = new IterativeDeepeningSearch<>();
            SearchAgent<FarmerState, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void FarmerWithGreedyBestFirstSearchMisplacedTileHeuristic() {
        try {
            System.out.println("\nFarmerAppDemo GreedyBestFirstSearch(MisplacedTileHeuristic) -->");
            Problem<FarmerState, Action> problem = FarmerFunction.createProblem();
            SearchForActions<FarmerState, Action> search = new GreedyBestFirstSearch<>(new GraphSearch<>(),
                    FarmerFunction.createMisplacedTileHeuristicFunction());
            SearchAgent<FarmerState, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void FarmerWithGreedyBestFirstSearchManhattanHeuristic() {
        try {
            System.out.println("\nFarmerAppDemo GreedyBestFirstSearch(ManhattanHeuristic) -->");
            Problem<FarmerState, Action> problem = FarmerFunction.createProblem();
            SearchForActions<FarmerState, Action> search = new GreedyBestFirstSearch<>(new GraphSearch<>(),
                    FarmerFunction.createManhattanHeuristicFunction());
            SearchAgent<FarmerState, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void FarmerWithAStarSearchMisplacedTileHeuristic() {
        try {
            System.out.println("\nFarmerAppDemo AStarSearch(MisplacedTileHeuristic) -->");
            Problem<FarmerState, Action> problem = FarmerFunction.createProblem();
            SearchForActions<FarmerState, Action> search = new AStarSearch<>(new GraphSearch<>(),
                    FarmerFunction.createMisplacedTileHeuristicFunction());
            SearchAgent<FarmerState, Action> agent = new SearchAgent<>(problem, search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void FarmerWithAStarSearchManhattanHeuristic() {
        try {
            System.out.println("\nFarmerAppDemo AStarSearch(ManhattanHeuristic) -->");
            Problem<FarmerState, Action> problem = FarmerFunction.createProblem();
            SearchForActions<FarmerState, Action> search = new AStarSearch<>(new GraphSearch<>(),
                    FarmerFunction.createManhattanHeuristicFunction());
            SearchAgent<FarmerState, Action> agent = new SearchAgent<>(problem, search);
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
