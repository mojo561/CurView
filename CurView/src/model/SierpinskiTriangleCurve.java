package model;

public class SierpinskiTriangleCurve extends LSystemJFX
{
	public SierpinskiTriangleCurve()
	{
		axiom = "F-G-G";
		
		variables.put("F", "F-G+F+G-F");
		variables.put("G", "GG");
		
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("G", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		
		turnAngleDegrees = 120;
	}
}