package model;

import java.util.Collection;
import java.util.Collections;

import javafx.concurrent.Task;
import javafx.geometry.BoundingBox;
import javafx.scene.shape.Line;

public final class LSystemBuilderTask extends Task<Collection<Line>>
{
	private Line origin;
	private int iterations;
	private LSystemJFX lsystem;
	private BoundingBox drawArea;

	public void setOrigin(Line origin)
	{
		if(origin != null)
		{
			this.origin = origin;
		}
	}
	
	public void setIterations(int iterations)
	{
		this.iterations = iterations;
	}
	
	public void setLSystem(LSystemJFX lsystem)
	{
		if(lsystem != null)
		{
			this.lsystem = lsystem;
		}
	}
	
	public void setDrawArea(BoundingBox drawArea)
	{
		this.drawArea = drawArea;
	}
	
	public BoundingBox getDrawArea()
	{
		if(drawArea == null)
		{
			return new BoundingBox(0, 0, 0, 0);
		}
		return new BoundingBox(
				drawArea.getMinX(),
				drawArea.getMinY(),
				drawArea.getMinZ(),
				drawArea.getWidth(),
				drawArea.getHeight(),
				drawArea.getDepth());
	}
	
	@Override
	protected Collection<Line> call() throws Exception
	{
		if(lsystem != null)
		{
			return lsystem.build(origin, iterations, drawArea);
		}
		return Collections.<Line>emptyList();
	}
}