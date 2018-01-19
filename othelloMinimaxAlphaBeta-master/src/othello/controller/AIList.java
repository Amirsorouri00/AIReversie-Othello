package othello.controller;

import othello.ai.*;

public class AIList {
	public static final ReversiAI AI[] = {
		new GreedyAI(),
		new MyPlayerAI(),
		new OtherPlayer(),
		new RandomAI(),
		};
	public static String[] getAINameList(){
		String ret[] = new String[AI.length];
		for(int i = 0; i < AI.length; i++)
			ret[i] = AI[i].getName();
		return ret;
	}
	public static ReversiAI getAIByName(String AIName){
		for (ReversiAI reversiAI : AI)
			if(reversiAI.getName().equals(AIName))
				try{
					return reversiAI.getClass().newInstance();
				}catch (Exception e) {
					return reversiAI;
				}
		return null;
	}
}
