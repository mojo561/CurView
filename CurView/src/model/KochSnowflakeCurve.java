package model;

public class KochSnowflakeCurve extends LSystemJFX
{
	public KochSnowflakeCurve()
	{
		axiom = "F--F--F";
		
		variables.put("F", "F+F--F+F");
		constants.put("F", operation.MOVE_FWD_DRAW);
		constants.put("+", operation.TURN_RIGHT);
		constants.put("-", operation.TURN_LEFT);
		
		turnAngleDegrees = 60;
	}
}