package model;

public class KochVariantBCurve extends LSystemJFX
{
	public KochVariantBCurve()
	{
		axiom = "F+F+F+F";
		
		variables.put("F", "FF+F+F+F+F+F-F");
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		
		turnAngleDegrees = 90;
	}
}
