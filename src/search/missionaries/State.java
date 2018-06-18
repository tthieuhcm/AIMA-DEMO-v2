package search.missionaries;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;



public class State {
	public static Action CANN1_MOVE_ALONE = new DynamicAction("CANN1_MOVE_ALONE");
	public static Action CANN2_MOVE_ALONE = new DynamicAction("CANN2_MOVE_ALONE");
	public static Action CANN3_MOVE_ALONE = new DynamicAction("CANN3_MOVE_ALONE");

	public static Action MISS1_MOVE_ALONE = new DynamicAction("MISS1_MOVE_ALONE");
	public static Action MISS2_MOVE_ALONE = new DynamicAction("MISS2_MOVE_ALONE");
	public static Action MISS3_MOVE_ALONE = new DynamicAction("MISS3_MOVE_ALONE");

	public static Action MISS1_MOVE_WITH_CANN1 = new DynamicAction("MISS1_MOVE_WITH_CANN1");
	public static Action MISS1_MOVE_WITH_CANN2 = new DynamicAction("MISS1_MOVE_WITH_CANN2");
	public static Action MISS1_MOVE_WITH_CANN3 = new DynamicAction("MISS1_MOVE_WITH_CANN3");

	public static Action MISS2_MOVE_WITH_CANN1 = new DynamicAction("MISS2_MOVE_WITH_CANN1");
	public static Action MISS2_MOVE_WITH_CANN2 = new DynamicAction("MISS2_MOVE_WITH_CANN2");
	public static Action MISS2_MOVE_WITH_CANN3 = new DynamicAction("MISS2_MOVE_WITH_CANN3");

	public static Action MISS3_MOVE_WITH_CANN1 = new DynamicAction("MISS3_MOVE_WITH_CANN1");
	public static Action MISS3_MOVE_WITH_CANN2 = new DynamicAction("MISS3_MOVE_WITH_CANN2");
	public static Action MISS3_MOVE_WITH_CANN3 = new DynamicAction("MISS3_MOVE_WITH_CANN3");

	public static Action MISS1_MOVE_WITH_MISS2 = new DynamicAction("MISS1_MOVE_WITH_MISS2");
	public static Action MISS1_MOVE_WITH_MISS3 = new DynamicAction("MISS1_MOVE_WITH_MISS3");
	public static Action MISS2_MOVE_WITH_MISS3 = new DynamicAction("MISS2_MOVE_WITH_MISS3");

	public static Action CANN1_MOVE_WITH_CANN2 = new DynamicAction("CANN1_MOVE_WITH_CANN2");
	public static Action CANN1_MOVE_WITH_CANN3 = new DynamicAction("CANN1_MOVE_WITH_CANN3");
	public static Action CANN2_MOVE_WITH_CANN3 = new DynamicAction("CANN2_MOVE_WITH_CANN3");

	public static final int WEST = 0;
	public static final int EAST = 1;
	public int m_state[] = {WEST, WEST, WEST, WEST, WEST, WEST, WEST};
	public State(){

	}

	public State(int state[]){
		this.m_state = new int[state.length];
		System.arraycopy(state, 0, m_state, 0, state.length);
	}

	public State(State copy) {
		this(copy.getState());
	}

	public int[] getState() {
		return m_state;
	}

	boolean isValidState(int[] state){
		int CANN_WEST = 0, CANN_EAST = 0, MISS_WEST = 0, MISS_EAST = 0;
		for (int i = 0; i < 3; i++) {
			if (state[i] == 0 ) MISS_WEST++; else MISS_EAST++;
			if (state[i+3] == 0 ) CANN_WEST++; else CANN_EAST++;
		};
		if ((MISS_WEST < CANN_WEST && MISS_WEST != 0) || (MISS_EAST < CANN_EAST && MISS_EAST != 0)) return false; else return true;
	}

	int move(int side){
		if (side == WEST) return EAST;
		else return WEST;
	}

	public boolean canMiss1MoveAlone(){
		int newState[] = {move(m_state[0]), m_state[1], m_state[2], m_state[3], m_state[4], m_state[5], move(m_state[6])};
		return isValidState(newState) && (m_state[0] == m_state[6]);
	}
	public boolean canMiss2MoveAlone(){
		int newState[] = {m_state[0], move(m_state[1]), m_state[2], m_state[3], m_state[4], m_state[5], move(m_state[6])};
		return isValidState(newState) && m_state[1] == m_state[6];
	}
	public boolean canMiss3MoveAlone(){
		int newState[] = {m_state[0], m_state[1], move(m_state[2]), m_state[3], m_state[4], m_state[5], move(m_state[6])};
		return isValidState(newState) && m_state[2] == m_state[6];
	}

	public boolean canCann1MoveAlone(){
		int newState[] = {m_state[0], m_state[1], m_state[2], move(m_state[3]), m_state[4], m_state[5], move(m_state[6])};
		return isValidState(newState) && m_state[3] == m_state[6];
	}
	public boolean canCann2MoveAlone(){
		int newState[] = {m_state[0], m_state[1], m_state[2], m_state[3], move(m_state[4]), m_state[5], move(m_state[6])};
		return isValidState(newState) && m_state[4] == m_state[6];
	}
	public boolean canCann3MoveAlone(){
		int newState[] = {m_state[0], m_state[1], m_state[2], m_state[3], m_state[4], move(m_state[5]), move(m_state[6])};
		return isValidState(newState) && m_state[5] == m_state[6];
	}

	public boolean canMiss1MoveWithCann1(){
		int newState[] = {move(m_state[0]), m_state[1], m_state[2], move(m_state[3]), m_state[4], m_state[5], move(m_state[6])};
		boolean cond1 = (m_state[0] == m_state[3] && m_state[0] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}
	public boolean canMiss1MoveWithCann2(){
		int newState[] = {move(m_state[0]), m_state[1], m_state[2], m_state[3], move(m_state[4]), m_state[5], move(m_state[6])};
		boolean cond1 = (m_state[0] == m_state[4] && m_state[0] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}
	public boolean canMiss1MoveWithCann3(){
		int newState[] = {move(m_state[0]), m_state[1], m_state[2], m_state[3], m_state[4], move(m_state[5]), move(m_state[6])};
		boolean cond1 = (m_state[0] == m_state[5] && m_state[0] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}

	public boolean canMiss2MoveWithCann1(){
		int newState[] = {m_state[0], move(m_state[1]), m_state[2], move(m_state[3]), m_state[4], m_state[5], move(m_state[6])};
		boolean cond1 = (m_state[1] == m_state[3] && m_state[1] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}
	public boolean canMiss2MoveWithCann2(){
		int newState[] = {m_state[0], move(m_state[1]), m_state[2], m_state[3], move(m_state[4]), m_state[5], move(m_state[6])};
		boolean cond1 = (m_state[1] == m_state[4] && m_state[1] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}
	public boolean canMiss2MoveWithCann3(){
		int newState[] = {m_state[0], move(m_state[1]), m_state[2], m_state[3], m_state[4], move(m_state[5]), move(m_state[6])};
		boolean cond1 = (m_state[1] == m_state[5] && m_state[1] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}

	public boolean canMiss3MoveWithCann1(){
		int newState[] = {m_state[0], m_state[1], move(m_state[2]), move(m_state[3]), m_state[4], m_state[5], move(m_state[6])};
		boolean cond1 = (m_state[2] == m_state[3] && m_state[2] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}
	public boolean canMiss3MoveWithCann2(){
		int newState[] = {m_state[0], m_state[1], move(m_state[2]), m_state[3], move(m_state[4]), m_state[5], move(m_state[6])};
		boolean cond1 = (m_state[2] == m_state[4] && m_state[2] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}
	public boolean canMiss3MoveWithCann3(){
		int newState[] = {m_state[0], m_state[1], move(m_state[2]), m_state[3], m_state[4], move(m_state[5]), move(m_state[6])};
		boolean cond1 = (m_state[2] == m_state[5] && m_state[2] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}

	public boolean canMiss1MoveWithMiss2(){
		int newState[] = {move(m_state[0]), move(m_state[1]), m_state[2], m_state[3], m_state[4], m_state[5], move(m_state[6])};
		boolean cond1 = (m_state[0] == m_state[1] && m_state[0] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}
	public boolean canMiss1MoveWithMiss3(){
		int newState[] = {move(m_state[0]), m_state[1], move(m_state[2]), m_state[3], m_state[4], m_state[5], move(m_state[6])};
		boolean cond1 = (m_state[0] == m_state[2] && m_state[0] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}
	public boolean canMiss2MoveWithMiss3(){
		int newState[] = {m_state[0], move(m_state[1]), move(m_state[2]), m_state[3], m_state[4], m_state[5], move(m_state[6])};
		boolean cond1 = (m_state[1] == m_state[2] && m_state[1] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}

	public boolean canCann1MoveWithCann2(){
		int newState[] = {m_state[0], m_state[1], m_state[2], move(m_state[3]), move(m_state[4]), m_state[5], move(m_state[6])};
		boolean cond1 = (m_state[3] == m_state[4] && m_state[3] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}
	public boolean canCann1MoveWithCann3(){
		int newState[] = {m_state[0], m_state[1], m_state[2], move(m_state[3]), m_state[4], move(m_state[5]), move(m_state[6])};
		boolean cond1 = (m_state[3] == m_state[5] && m_state[3] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}
	public boolean canCann2MoveWithCann3(){
		int newState[] = {m_state[0], m_state[1], m_state[2], m_state[3], move(m_state[4]), move(m_state[5]), move(m_state[6])};
		boolean cond1 = (m_state[4] == m_state[5] && m_state[4] == m_state[6]);
		boolean cond2 = isValidState(newState);
		return (cond1 && cond2);
	}

	State miss1MoveAlone(){
		int newState[] = {move(m_state[0]), m_state[1], m_state[2], m_state[3], m_state[4], m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State miss2MoveAlone(){
		int newState[] = {m_state[0], move(m_state[1]), m_state[2], m_state[3], m_state[4], m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State miss3MoveAlone(){
		int newState[] = {m_state[0], m_state[1], move(m_state[2]), m_state[3], m_state[4], m_state[5], move(m_state[6])};
		return new State(newState);
	}

	State cann1MoveAlone(){
		int newState[] = {m_state[0], m_state[1], m_state[2], move(m_state[3]), m_state[4], m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State cann2MoveAlone(){
		int newState[] = {m_state[0], m_state[1], m_state[2], m_state[3], move(m_state[4]), m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State cann3MoveAlone(){
		int newState[] = {m_state[0], m_state[1], m_state[2], m_state[3], m_state[4], move(m_state[5]), move(m_state[6])};
		return new State(newState);
	}

	State miss1MoveWithCann1(){
		int newState[] = {move(m_state[0]), m_state[1], m_state[2], move(m_state[3]), m_state[4], m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State miss1MoveWithCann2(){
		int newState[] = {move(m_state[0]), m_state[1], m_state[2], m_state[3], move(m_state[4]), m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State miss1MoveWithCann3(){
		int newState[] = {move(m_state[0]), m_state[1], m_state[2], m_state[3], m_state[4], move(m_state[5]), move(m_state[6])};
		return new State(newState);
	}

	State miss2MoveWithCann1(){
		int newState[] = {m_state[0], move(m_state[1]), m_state[2], move(m_state[3]), m_state[4], m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State miss2MoveWithCann2(){
		int newState[] = {m_state[0], move(m_state[1]), m_state[2], m_state[3], move(m_state[4]), m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State miss2MoveWithCann3(){
		int newState[] = {m_state[0], move(m_state[1]), m_state[2], m_state[3], m_state[4], move(m_state[5]), move(m_state[6])};
		return new State(newState);
	}

	State miss3MoveWithCann1(){
		int newState[] = {m_state[0], m_state[1], move(m_state[2]), move(m_state[3]), m_state[4], m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State miss3MoveWithCann2(){
		int newState[] = {m_state[0], m_state[1], move(m_state[2]), m_state[3], move(m_state[4]), m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State miss3MoveWithCann3(){
		int newState[] = {m_state[0], m_state[1], move(m_state[2]), m_state[3], m_state[4], move(m_state[5]), move(m_state[6])};
		return new State(newState);
	}

	State miss1MoveWithMiss2(){
		int newState[] = {move(m_state[0]), move(m_state[1]), m_state[2], m_state[3], m_state[4], m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State miss1MoveWithMiss3(){
		int newState[] = {move(m_state[0]), m_state[1], move(m_state[2]), m_state[3], m_state[4], m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State miss2MoveWithMiss3(){
		int newState[] = {m_state[0], move(m_state[1]), move(m_state[2]), m_state[3], m_state[4], m_state[5], move(m_state[6])};
		return new State(newState);
	}

	State cann1MoveWithCann2(){
		int newState[] = {m_state[0], m_state[1], m_state[2], move(m_state[3]), move(m_state[4]), m_state[5], move(m_state[6])};
		return new State(newState);
	}
	State cann1MoveWithCann3(){
		int newState[] = {m_state[0], m_state[1], m_state[2], move(m_state[3]), m_state[4], move(m_state[5]), move(m_state[6])};
		return new State(newState);
	}
	State cann2MoveWithCann3(){
		int newState[] = {m_state[0], m_state[1], m_state[2], m_state[3], move(m_state[4]), move(m_state[5]), move(m_state[6])};
		return new State(newState);
	}

	private String num2String(int side){
		if (side == WEST) return "WEST";
		else return "EAST";
	}
	public String toString(){
		String result = "";
		result = "[ MISS1=" + num2String(m_state[0]) + "," +
					"MISS2=" + num2String(m_state[1]) + "," +
					"MISS3=" + num2String(m_state[2]) + "," +
					"CANN1=" + num2String(m_state[3]) + "," +
					"CANN2=" + num2String(m_state[4]) + "," +
					"CANN3=" + num2String(m_state[5]) + " ]";
		return result;
	}
}
