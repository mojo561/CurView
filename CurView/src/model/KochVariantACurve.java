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
public class KochVariantACurve extends LSystemJFX
{
	public KochVariantACurve()
	{
		axiom = "F-F-F-F";
		
		variables.put("F", "FF-F-F-F-FF");
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		
		turnAngle = 90;
	}
}