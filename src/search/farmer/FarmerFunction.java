package search.farmer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

import aima.core.agent.Action;

import aima.core.search.framework.Node;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;

public class FarmerFunction {
	
	public static Problem<FarmerState, Action> createProblem() {
        return new GeneralProblem<>(new FarmerState(), FarmerFunction::getActions, FarmerFunction::getResult, FarmerFunction::testGoal);
    }
	
	public static List<Action> getActions(FarmerState state) {
		List<Action> actions = new ArrayList<>();
		
		if(state.canFamerMoveAlone()) actions.add(FarmerState.FARMER_MOVE_ALONE);
		if(state.canFamerMoveWithWolf()) actions.add(FarmerState.FARMER_MOVE_WOLF);
		if(state.canFamerMoveWithSheep()) actions.add(FarmerState.FARMER_MOVE_SHEEP);
		if(state.canFamerMoveWithCabbage()) actions.add(FarmerState.FARMER_MOVE_CABBAGE);
		
		return actions;
	}
	
	public static FarmerState getResult(FarmerState state, Action action) {
		FarmerState result = null;
		if (FarmerState.FARMER_MOVE_ALONE.equals(action))
			result = state.moveAlone();
		if (FarmerState.FARMER_MOVE_WOLF.equals(action))
			result = state.moveWithWolf();
		if (FarmerState.FARMER_MOVE_SHEEP.equals(action))
			result = state.moveWithSheep();
		if (FarmerState.FARMER_MOVE_CABBAGE.equals(action))
			result = state.moveWithCabbage();
		return result;
	}
	
	public static boolean testGoal(FarmerState state) {
		int goalState[] = {1, 1, 1, 1};
		boolean flag = true;
		for(int i=0; i< 4; i++){
			if(state.m_state[i] != goalState[i]){
				flag = false;
				break;
			}
		}
		return flag;
    }
	
	public static ToDoubleFunction<Node<FarmerState, Action>> createManhattanHeuristicFunction() {
		return new ManhattanHeuristicFunction();
	}

	public static ToDoubleFunction<Node<FarmerState, Action>> createMisplacedTileHeuristicFunction() {
		return new MisplacedTileHeuristicFunction();
	}

    private static class ManhattanHeuristicFunction implements ToDoubleFunction<Node<FarmerState, Action>> {

        @Override
        public double applyAsDouble(Node<FarmerState, Action> node) {
        	FarmerState state = node.getState();
            int retVal = 0;
            for (int i = 1; i < 4; i++) {
                int FarmerPos = state.m_state[0];
                retVal += evaluateManhattanDistanceOf(FarmerPos, state.m_state[i]);
            }
            return retVal;
        }

        private int evaluateManhattanDistanceOf(int FarmerPos, int Pos) {
        	int retVal = 0;
            if (FarmerPos != Pos && FarmerPos == FarmerState.EAST) retVal = 2;
            else if (FarmerPos != Pos && FarmerPos == FarmerState.WEST) retVal = 0;
            else if (FarmerPos == Pos && FarmerPos == FarmerState.WEST) retVal = 1;
        	return retVal;
        }
    }

    private  static class MisplacedTileHeuristicFunction implements ToDoubleFunction<Node<FarmerState, Action>> {

        public double applyAsDouble(Node<FarmerState, Action> node) {
        	FarmerState state = (FarmerState) node.getState();
            return getNumberOfMisplacedTiles(state);
        }

        private int getNumberOfMisplacedTiles(FarmerState state) {
            int numberOfMisplacedTiles = 0;
            if (state.m_state[1] != FarmerState.EAST) {
                numberOfMisplacedTiles++;
            }
            if (state.m_state[2] != FarmerState.EAST) {
                numberOfMisplacedTiles++;
            }
            if (state.m_state[3] != FarmerState.EAST) {
                numberOfMisplacedTiles++;
            }
            return numberOfMisplacedTiles;
        }
    }
}

