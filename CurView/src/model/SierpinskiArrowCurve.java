package model;

public class SierpinskiArrowCurve extends LSystemJFX
{
	public SierpinskiArrowCurve()
	{
		axiom = "XF";
		
		variables.put("X", "YF+XF+Y");
		variables.put("Y", "XF-YF-X");
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		
		turnAngle = 60;
	}
}