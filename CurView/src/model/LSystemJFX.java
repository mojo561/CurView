package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public abstract class LSystemJFX extends LSystem<Line, BoundingBox>
{
	private final int MAX_COLLECTIONSIZE = 500000;
	
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
	public Collection<Line> build(Line origin, int iterations, BoundingBox boundingBox)
	{
		if(origin == null || iterations < 0 || boundingBox == null)
		{
			return Collections.<Line>emptyList();
		}
		
		Collection<Line> rvalCollection = new ArrayList<Line>();
		Deque<Line> stack = new ArrayDeque<Line>();
		Line currentPosition = new Line(origin.getStartX(), origin.getStartY(), origin.getEndX(), origin.getEndY());
		String lsysSequence;
		Translate translation;
		Rotate rotation;
		Point2D p2d;
		
		try
		{
			lsysSequence = buildStringSequence(iterations);
		}
		catch(OutOfMemoryError e)
		{
			System.err.println( String.format("Error during LSystem String sequence build: \"%s\", using axiom as the output sequence", e) );
			lsysSequence = axiom;
		}
		
		for(int i = 0; i < lsysSequence.length(); ++i)
		{
			if(rvalCollection.size() >= MAX_COLLECTIONSIZE)
			{
				System.err.println( String.format("Warning: too many LSystem elements, stopping at size %d", rvalCollection.size()) );
				break;
			}
			
			String cstr = String.valueOf( lsysSequence.charAt(i) );
			if(!constants.containsKey(cstr))
			{
				continue;
			}
			switch(constants.get(cstr))
			{
				case MOVE_FWD_DRAW:
					
					Line newLine = new Line(currentPosition.getStartX(), currentPosition.getStartY(), currentPosition.getEndX(), currentPosition.getEndY());
					Point2D newLineTail = new Point2D(newLine.getStartX(), newLine.getStartY());
					Point2D newLineHead = new Point2D(newLine.getEndX(), newLine.getEndY());
					if(boundingBox.contains(newLineTail) || boundingBox.contains(newLineHead))
					{
						rvalCollection.add( newLine );
					}
					
					translation = new Translate(currentPosition.getEndX(), currentPosition.getEndY());
					p2d = translation.transform(currentPosition.getEndX() - currentPosition.getStartX(), currentPosition.getEndY() - currentPosition.getStartY());
					currentPosition.setStartX(currentPosition.getEndX());
					currentPosition.setStartY(currentPosition.getEndY());
					currentPosition.setEndX(p2d.getX());
					currentPosition.setEndY(p2d.getY());
					break;
				case MOVE_FWD_NODRAW:
					translation = new Translate(currentPosition.getEndX(), currentPosition.getEndY());
					p2d = translation.transform(currentPosition.getEndX() - currentPosition.getStartX(), currentPosition.getEndY() - currentPosition.getStartY());
					currentPosition.setStartX(currentPosition.getEndX());
					currentPosition.setStartY(currentPosition.getEndY());
					currentPosition.setEndX(p2d.getX());
					currentPosition.setEndY(p2d.getY());
					break;
				case POP_STATE:
					currentPosition = doStackPop(stack);
					break;
				case PUSH_STATE:
					currentPosition = doStackPush(currentPosition, stack);
					break;
				case TURN_LEFT:
					rotation = new Rotate(turnAngleDegrees, currentPosition.getStartX(), currentPosition.getStartY());
					p2d = rotation.transform(currentPosition.getEndX(), currentPosition.getEndY());
					currentPosition.setEndX(p2d.getX());
					currentPosition.setEndY(p2d.getY());
					break;
				case TURN_RIGHT:
					rotation = new Rotate(turnAngleDegrees * -1, currentPosition.getStartX(), currentPosition.getStartY());
					p2d = rotation.transform(currentPosition.getEndX(), currentPosition.getEndY());
					currentPosition.setEndX(p2d.getX());
					currentPosition.setEndY(p2d.getY());
					break;
			}
		}
		
		return rvalCollection;
	}
}