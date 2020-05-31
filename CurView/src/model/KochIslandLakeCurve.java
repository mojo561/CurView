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
public class KochIslandLakeCurve extends LSystemJFX
{
	public KochIslandLakeCurve()
	{
		axiom = "F+F+F+F";
		
		variables.put("F", "F+f-FF+F+FF+Ff+FF-f+FF-F-FF-Ff-FFF");
		variables.put("f", "ffffff");
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("f", LSystem.operation.MOVE_FWD_NODRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		
		turnAngle = 90;
	}
}