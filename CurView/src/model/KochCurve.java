package model;

public final class KochCurve extends LSystemJFX
{
	public KochCurve()
	{
		axiom = "F";
		
		variables.put("F", "F+F-F-F+F");
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		
		turnAngleDegrees = 90;
	}
}