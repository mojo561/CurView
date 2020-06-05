package model;

public class PlantCurve extends LSystemJFX
{
	public PlantCurve()
	{
		axiom = "X";
		
		variables.put("X", "F+[[X]-X]-F[-FX]+X");
		variables.put("F", "FF");
		
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		constants.put("[", LSystem.operation.PUSH_STATE);
		constants.put("]", LSystem.operation.POP_STATE);
		
		turnAngleDegrees = 25;
	}
}