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
public class HilbertCurve extends LSystemJFX
{
	public HilbertCurve()
	{
		axiom = "A";
		
		variables.put("A", "-BF+AFA+FB-");
		variables.put("B", "+AF-BFB-FA+");
		
		constants.put("F", LSystem.operation.MOVE_FWD_DRAW);
		constants.put("+", LSystem.operation.TURN_LEFT);
		constants.put("-", LSystem.operation.TURN_RIGHT);
		
		turnAngle = 90;
	}
}