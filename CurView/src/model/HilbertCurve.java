package model;

public final class HilbertCurve extends LSystemJFX
{
	public HilbertCurve()
	{
		axiom = "A";
		
		variables.put("A", "-BF+AFA+FB-");
		variables.put("B", "+AF-BFB-FA+");
		
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		
		turnAngleDegrees = 90;
	}
}