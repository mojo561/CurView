/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import javafx.scene.shape.Line;

/**
 *
 * @author mojo
 */
public abstract class LSystemJFX extends LSystem<Line>
{

	@Override
	public Collection<Line> build(Line origin, int iterations)
	{
		Collection<Line> rvalCollection = new ArrayList<Line>();
		Deque<Line> stack = new ArrayDeque<Line>();
		Line currentPosition = new Line(origin.getStartX(), origin.getStartY(), origin.getEndX(), origin.getEndY());
		String lsysSequence = buildStringSequence(iterations);
		
		return rvalCollection;
	}
}