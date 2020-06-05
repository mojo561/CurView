package control;

import java.util.Collection;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.shape.Line;
import model.BinaryCurve;
import model.HilbertCurve;
import model.KochCurve;
import model.KochIslandLakeCurve;
import model.KochSnowflakeCurve;
import model.KochVariantACurve;
import model.LSystemJFX;
import model.PlantCurve;
import model.SierpinskiArrowCurve;
import model.SierpinskiTriangleCurve;

public class MainWindowController
{
	@FXML
	private Button buttonCmdDraw;
	
	@FXML
	private TabPane tabpane;
	
	/*****************
	 * TAB
	 *****************/
	@FXML
	private Tab tabHilbertCurve;
	
	@FXML
	private Tab tabKochCurve;
	
	@FXML
	private Tab tabKochSnowflake;
	
	@FXML
	private Tab tabKochIL;
	
	@FXML
	private Tab tabKochVariantA;
	
	@FXML
	private Tab tabSierpinskiArrow;
	
	@FXML
	private Tab tabSierpinskiTriangle;
	
	@FXML
	private Tab tabBinaryTree;
	
	@FXML
	private Tab tabPlant;
	
	/*****************
	 * CANVAS
	 *****************/
	@FXML
	private Canvas canvasHilbertDrawTarget;
	
	@FXML
	private Canvas canvasKochDrawTarget;
	
	@FXML
	private Canvas canvasKochSnowflakeDrawTarget;
	
	@FXML
	private Canvas canvasKochILDrawTarget;
	
	@FXML
	private Canvas canvasKochVariantADrawTarget;
	
	@FXML
	private Canvas canvasSierpinskiArrowDrawTarget;
	
	@FXML
	private Canvas canvasSierpinskiTriangleDrawTarget;
	
	@FXML
	private Canvas canvasBinaryTreeDrawTarget;
	
	@FXML
	private Canvas canvasPlantDrawTarget;
	
	/**
	 * Not included in GUI. This is just holds references to other Canvas objects.
	 */
	private Canvas canvasCurrentDrawTarget;
	
	/*****************
	 * SLIDER
	 *****************/
	@FXML
	private Slider sliderStartX;
	
	@FXML
	private Slider sliderStartY;
	
	@FXML
	private Slider sliderIterations;
	
	@FXML
	private Slider sliderLineWidth;
	
	private HashMap<Tab, Canvas> tabCanvasMap;
	private HashMap<Canvas, LSystemJFX> canvasLSystemMap;
	private final int MAX_WIDTH;
	private final int MAX_HEIGHT;
	
	public MainWindowController()
	{
		MAX_WIDTH = 4000;//8192;
		MAX_HEIGHT = 4000;//8192;
	}
	
	@FXML
	private void initialize()
	{
		tabCanvasMap = new HashMap<>();
		canvasLSystemMap = new HashMap<>();
		
		tabCanvasMap.put(tabHilbertCurve, canvasHilbertDrawTarget);
		canvasLSystemMap.put(canvasHilbertDrawTarget, new HilbertCurve());
		
		tabCanvasMap.put(tabKochCurve, canvasKochDrawTarget);
		canvasLSystemMap.put(canvasKochDrawTarget, new KochCurve());
		
		tabCanvasMap.put(tabKochSnowflake, canvasKochSnowflakeDrawTarget);
		canvasLSystemMap.put(canvasKochSnowflakeDrawTarget, new KochSnowflakeCurve());
		
		tabCanvasMap.put(tabKochIL, canvasKochILDrawTarget);
		canvasLSystemMap.put(canvasKochILDrawTarget, new KochIslandLakeCurve());
		
		tabCanvasMap.put(tabKochVariantA, canvasKochVariantADrawTarget);
		canvasLSystemMap.put(canvasKochVariantADrawTarget, new KochVariantACurve());
		
		tabCanvasMap.put(tabSierpinskiArrow, canvasSierpinskiArrowDrawTarget);
		canvasLSystemMap.put(canvasSierpinskiArrowDrawTarget, new SierpinskiArrowCurve());
		
		tabCanvasMap.put(tabSierpinskiTriangle, canvasSierpinskiTriangleDrawTarget);
		canvasLSystemMap.put(canvasSierpinskiTriangleDrawTarget, new SierpinskiTriangleCurve());
		
		tabCanvasMap.put(tabBinaryTree, canvasBinaryTreeDrawTarget);
		canvasLSystemMap.put(canvasBinaryTreeDrawTarget, new BinaryCurve());
		
		tabCanvasMap.put(tabPlant, canvasPlantDrawTarget);
		canvasLSystemMap.put(canvasPlantDrawTarget, new PlantCurve());
		
		canvasCurrentDrawTarget = tabCanvasMap.get( tabpane.getTabs().get(0) );

		tabpane.getSelectionModel().selectedItemProperty().addListener( (obs, oldTab, newTab) -> {
			Canvas oldCanvas = tabCanvasMap.get(oldTab);
			GraphicsContext gctx = oldCanvas.getGraphicsContext2D();
			gctx.clearRect(0, 0, oldCanvas.getWidth(), oldCanvas.getHeight());
			oldCanvas.setWidth(100);
			oldCanvas.setHeight(100);
			
			canvasCurrentDrawTarget = tabCanvasMap.get(newTab);
			canvasCurrentDrawTarget.setVisible(true);
		});
	}
	
	@FXML
	private void cmdDraw()
	{
		if(canvasCurrentDrawTarget == null)
		{
			return;
		}
		
		canvasCurrentDrawTarget.setWidth(MAX_WIDTH);
		canvasCurrentDrawTarget.setHeight(MAX_HEIGHT);
		
		GraphicsContext gctx = canvasCurrentDrawTarget.getGraphicsContext2D();
		int lineLength = (int)sliderLineWidth.getValue();
		int iterations = (int)sliderIterations.getValue();
		
		//TODO: until we add something to the GUI in the future, we can control the starting rotation of the resulting curve here
		Point2D pstart;
		Line lstart;
		if(canvasCurrentDrawTarget == canvasHilbertDrawTarget)
		{
			pstart = new Point2D(0, lineLength);
		}
		else
		{
			pstart = new Point2D(lineLength, 0);
		}
		lstart = new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue() + pstart.getX(), sliderStartY.getValue() + pstart.getY());
		
		Collection<Line> lines = canvasLSystemMap.get(canvasCurrentDrawTarget).build(lstart, iterations);
		
		gctx.clearRect(0, 0, canvasCurrentDrawTarget.getWidth(), canvasCurrentDrawTarget.getHeight());
		
		for(Line line : lines)
		{
			gctx.beginPath();
			gctx.moveTo(line.getStartX(), line.getStartY());
			gctx.lineTo(line.getEndX(), line.getEndY());
			
			gctx.setLineWidth(2);
			
			gctx.closePath();
			gctx.stroke();
		}
	}
}