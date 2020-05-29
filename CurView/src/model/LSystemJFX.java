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
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 *
 * @author mojo
 */
public abstract class LSystemJFX extends LSystem<Line>
{
	@Override
	protected Line doStackPush(Line line, Deque<Line> stack)
	{
		stack.push(line);
		return new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
	}
	
	@Override
	protected Line doStackPop(Deque<Line> stack)
	{
		if(stack.isEmpty())
		{
			return null;
		}
		return stack.pop();
	}

	@Override
	public Collection<Line> build(Line origin, int iterations)
	{
		Collection<Line> rvalCollection = new ArrayList<Line>();
		Deque<Line> stack = new ArrayDeque<Line>();
		Line currentPosition = new Line(origin.getStartX(), origin.getStartY(), origin.getEndX(), origin.getEndY());
		String lsysSequence = buildStringSequence(iterations);
		Translate translation;
		Rotate rotation;
		Point2D p2d;
		
		for(int i = 0; i < lsysSequence.length(); ++i)
		{
			String cstr = String.valueOf( lsysSequence.charAt(i) );
			if(!constants.containsKey(cstr))
			{
				continue;
			}
			switch(constants.get(cstr))
			{
				case MOVE_FWD_DRAW:
					rvalCollection.add( new Line(currentPosition.getStartX(), currentPosition.getStartY(), currentPosition.getEndX(), currentPosition.getEndY()) );
					translation = new Translate(currentPosition.getEndX(), currentPosition.getEndY());
					p2d = translation.transform(currentPosition.getEndX() - currentPosition.getStartX(), currentPosition.getEndY() - currentPosition.getStartY());
					currentPosition.setStartX(currentPosition.getEndX());
					currentPosition.setStartY(currentPosition.getEndY());
					currentPosition.setEndX(p2d.getX());
					currentPosition.setEndY(p2d.getY());
					
					break;
				case MOVE_FWD_NODRAW:
					//TODO
					break;
				case POP_STATE:
					currentPosition = doStackPop(stack);
					break;
				case PUSH_STATE:
					currentPosition = doStackPush(currentPosition, stack);
					break;
				case TURN_LEFT:
					rotation = new Rotate(turnAngle, currentPosition.getStartX(), currentPosition.getStartY());
					p2d = rotation.transform(currentPosition.getEndX(), currentPosition.getEndY());
					currentPosition.setEndX(p2d.getX());
					currentPosition.setEndY(p2d.getY());
					break;
				case TURN_RIGHT:
					rotation = new Rotate(turnAngle * -1, currentPosition.getStartX(), currentPosition.getStartY());
					p2d = rotation.transform(currentPosition.getEndX(), currentPosition.getEndY());
					currentPosition.setEndX(p2d.getX());
					currentPosition.setEndY(p2d.getY());
					break;
			}
		}
		
		return rvalCollection;
	}
}