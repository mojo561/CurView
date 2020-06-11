package model;

public final class KochVariantACurve extends LSystemJFX
{
	public KochVariantACurve()
	{
		axiom = "F-F-F-F";
		
		variables.put("F", "FF-F-F-F-FF");
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		
		turnAngleDegrees = 90;
	}
}