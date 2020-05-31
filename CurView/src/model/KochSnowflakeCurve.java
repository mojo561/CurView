/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author mojo
 */
public class KochSnowflakeCurve extends LSystemJFX
{
	public KochSnowflakeCurve()
	{
		axiom = "F--F--F";
		
		variables.put("F", "F+F--F+F");
		constants.put("F", operation.MOVE_FWD_DRAW);
		constants.put("+", operation.TURN_RIGHT);
		constants.put("-", operation.TURN_LEFT);
		
		turnAngle = 60;
	}
}