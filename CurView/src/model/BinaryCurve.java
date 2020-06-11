package model;

import java.util.Deque;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

public final class BinaryCurve extends LSystemJFX
{
	public BinaryCurve()
	{
		axiom = "0";

		variables.put("1", "11");
		variables.put("0", "1[0]0");
		
		constants.put("1", operation.MOVE_FWD_DRAW);
		constants.put("0", operation.MOVE_FWD_DRAW);
		constants.put("[", operation.PUSH_STATE);
		constants.put("]", operation.POP_STATE);
		
		turnAngleDegrees = 45f;
	}

	/**
	 * Specific to binary tree curves as far as I know... Push a line to 'stack', then return a rotated copy of the line.
	 * @param line
	 * @param stack
	 * @return 
	 */
	@Override
	protected Line doStackPush(Line line, Deque<Line> stack)
	{
		stack.push(line);
		Line rval = new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
		Rotate rot = new Rotate(turnAngleDegrees, rval.getStartX(), rval.getStartY());
		Point2D p2d = rot.transform(rval.getEndX(), rval.getEndY());
		rval.setEndX(p2d.getX());
		rval.setEndY(p2d.getY());
		return rval;
	}
	
	/**
	 * Specific to binary tree curves as far as I know... Pop a line from 'stack', rotate it, then return it
	 * @param stack
	 * @return 
	 */
	@Override
	protected Line doStackPop(Deque<Line> stack)
	{
		if(stack.isEmpty())
		{
			return null;
		}
		Line rval = stack.pop();
		Rotate rot = new Rotate(turnAngleDegrees * -1, rval.getStartX(), rval.getStartY());
		Point2D p2d = rot.transform(rval.getEndX(), rval.getEndY());
		rval.setEndX(p2d.getX());
		rval.setEndY(p2d.getY());
		return rval;
	}
}