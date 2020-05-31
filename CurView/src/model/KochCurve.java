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
public class KochCurve extends LSystemJFX
{
	public KochCurve()
	{
		axiom = "F";
		
		variables.put("F", "F+F-F-F+F");
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		
		turnAngle = 90;
	}
}