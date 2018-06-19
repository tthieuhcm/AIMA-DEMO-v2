package search.missionaries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

import aima.core.agent.Action;

import aima.core.search.framework.Node;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;

public class Function {

    public static Problem<State, Action> createProblem() {
        return new GeneralProblem<>(new State(), Function::getActions, Function::getResult, Function::testGoal);
    }

    public static List<Action> getActions(State state) {
        List<Action> actions = new ArrayList<>();

        if (state.canCann1MoveAlone()) actions.add(State.CANN1_MOVE_ALONE);
        if (state.canCann2MoveAlone()) actions.add(State.CANN2_MOVE_ALONE);
        if (state.canCann3MoveAlone()) actions.add(State.CANN3_MOVE_ALONE);

        if (state.canMiss1MoveAlone()) actions.add(State.MISS1_MOVE_ALONE);
        if (state.canMiss2MoveAlone()) actions.add(State.MISS2_MOVE_ALONE);
        if (state.canMiss3MoveAlone()) actions.add(State.MISS3_MOVE_ALONE);

        if (state.canMiss1MoveWithCann1()) actions.add(State.MISS1_MOVE_WITH_CANN1);
        if (state.canMiss1MoveWithCann2()) actions.add(State.MISS1_MOVE_WITH_CANN2);
        if (state.canMiss1MoveWithCann3()) actions.add(State.MISS1_MOVE_WITH_CANN3);

        if (state.canMiss2MoveWithCann1()) actions.add(State.MISS2_MOVE_WITH_CANN1);
        if (state.canMiss2MoveWithCann2()) actions.add(State.MISS2_MOVE_WITH_CANN2);
        if (state.canMiss2MoveWithCann3()) actions.add(State.MISS2_MOVE_WITH_CANN3);

        if (state.canMiss3MoveWithCann1()) actions.add(State.MISS3_MOVE_WITH_CANN1);
        if (state.canMiss3MoveWithCann2()) actions.add(State.MISS3_MOVE_WITH_CANN2);
        if (state.canMiss3MoveWithCann3()) actions.add(State.MISS3_MOVE_WITH_CANN3);

        if (state.canMiss1MoveWithMiss2()) actions.add(State.MISS1_MOVE_WITH_MISS2);
        if (state.canMiss1MoveWithMiss3()) actions.add(State.MISS1_MOVE_WITH_MISS3);
        if (state.canMiss2MoveWithMiss3()) actions.add(State.MISS2_MOVE_WITH_MISS3);

        if (state.canCann1MoveWithCann2()) actions.add(State.CANN1_MOVE_WITH_CANN2);
        if (state.canCann1MoveWithCann3()) actions.add(State.CANN1_MOVE_WITH_CANN3);
        if (state.canCann2MoveWithCann3()) actions.add(State.CANN2_MOVE_WITH_CANN3);

        return actions;
    }

    public static State getResult(State state, Action action) {
        State result = null;

        if (State.CANN1_MOVE_ALONE.equals(action))
            result = state.cann1MoveAlone();
        if (State.CANN2_MOVE_ALONE.equals(action))
            result = state.cann2MoveAlone();
        if (State.CANN3_MOVE_ALONE.equals(action))
            result = state.cann3MoveAlone();

        if (State.MISS1_MOVE_ALONE.equals(action))
            result = state.miss1MoveAlone();
        if (State.MISS2_MOVE_ALONE.equals(action))
            result = state.miss2MoveAlone();
        if (State.MISS3_MOVE_ALONE.equals(action))
            result = state.miss3MoveAlone();

        if (State.MISS1_MOVE_WITH_CANN1.equals(action))
            result = state.miss1MoveWithCann1();
        if (State.MISS1_MOVE_WITH_CANN2.equals(action))
            result = state.miss1MoveWithCann2();
        if (State.MISS1_MOVE_WITH_CANN3.equals(action))
            result = state.miss1MoveWithCann3();

        if (State.MISS2_MOVE_WITH_CANN1.equals(action))
            result = state.miss2MoveWithCann1();
        if (State.MISS2_MOVE_WITH_CANN2.equals(action))
            result = state.miss2MoveWithCann2();
        if (State.MISS2_MOVE_WITH_CANN3.equals(action))
            result = state.miss2MoveWithCann3();

        if (State.MISS3_MOVE_WITH_CANN1.equals(action))
            result = state.miss3MoveWithCann1();
        if (State.MISS3_MOVE_WITH_CANN2.equals(action))
            result = state.miss3MoveWithCann2();
        if (State.MISS3_MOVE_WITH_CANN3.equals(action))
            result = state.miss3MoveWithCann3();

        if (State.MISS1_MOVE_WITH_MISS2.equals(action))
            result = state.miss1MoveWithMiss2();
        if (State.MISS1_MOVE_WITH_MISS3.equals(action))
            result = state.miss1MoveWithMiss3();
        if (State.MISS2_MOVE_WITH_MISS3.equals(action))
            result = state.miss2MoveWithMiss3();

        if (State.CANN1_MOVE_WITH_CANN2.equals(action))
            result = state.cann1MoveWithCann2();
        if (State.CANN1_MOVE_WITH_CANN3.equals(action))
            result = state.cann1MoveWithCann3();
        if (State.CANN2_MOVE_WITH_CANN3.equals(action))
            result = state.cann2MoveWithCann3();

        return result;
    }

    public static boolean testGoal(State state) {
        int goalState[] = {1, 1, 1, 1, 1, 1};
        boolean flag = true;
        for (int i = 0; i < 6; i++) {
            if (state.m_state[i] != goalState[i]) {
                flag = false;
                break;
            }
        }
        return flag;
    }
    
        public static ToDoubleFunction<Node<State, Action>> createManhattanHeuristicFunction() {
        return new ManhattanHeuristicFunction();
    }

    public static ToDoubleFunction<Node<State, Action>> createMisplacedTileHeuristicFunction() {
        return new MisplacedTileHeuristicFunction();
    }
    
    private static class ManhattanHeuristicFunction implements ToDoubleFunction<Node<State, Action>> {

        @Override
        public double applyAsDouble(Node<State, Action> node) {
            State state = node.getState();
            int retVal = 0;
            for (int i = 0; i < 6; i++) {
                int BoatPos = state.m_state[6];
                retVal += evaluateManhattanDistanceOf(BoatPos, state.m_state[i]);
            }
            if (retVal == 12) retVal = 11;
            return retVal;
        }

        private int evaluateManhattanDistanceOf(int BoatPos, int Pos) {
            int retVal = 0;
            //thuyền = Đông, đối tượng = Tây
            if (BoatPos != Pos && BoatPos == State.EAST) retVal = 2;
            //thuyền = Tây, đối tượng = Đông
            else if (BoatPos != Pos && BoatPos == State.WEST) retVal = 0;
            //thuyền = Tây, đối tượng = Tây
            else if (BoatPos == Pos && BoatPos == State.WEST) retVal = 1;
            //thuyền = Đông, đối tượng = Đông
            return retVal;
        }
    }
    
    private  static class MisplacedTileHeuristicFunction implements ToDoubleFunction<Node<State, Action>> {

        public double applyAsDouble(Node<State, Action> node) {
            State state = (State) node.getState();
            return getNumberOfMisplacedTiles(state);
        }

        private int getNumberOfMisplacedTiles(State state) {
            int numberOfMisplacedTiles = 0;
            if (state.m_state[0] != State.EAST) {
                numberOfMisplacedTiles++;
            }
            if (state.m_state[1] != State.EAST) {
                numberOfMisplacedTiles++;
            }
            if (state.m_state[2] != State.EAST) {
                numberOfMisplacedTiles++;
            }
            if (state.m_state[3] != State.EAST) {
                numberOfMisplacedTiles++;
            }
            if (state.m_state[4] != State.EAST) {
                numberOfMisplacedTiles++;
            }
            if (state.m_state[5] != State.EAST) {
                numberOfMisplacedTiles++;
            }
            return numberOfMisplacedTiles;
        }
    }
}
