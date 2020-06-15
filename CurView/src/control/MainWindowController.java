package control;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Line;
import model.BinaryCurve;
import model.HilbertCurve;
import model.KochCurve;
import model.KochIslandLakeCurve;
import model.KochSnowflakeCurve;
import model.KochVariantACurve;
import model.LSystemBuilderTask;
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
	
	//TODO: test
//	@FXML
//	private ScrollPane scrollPaneTest;
	
	private HashSet<Node> mapPausableNodes;
	private HashMap<Tab, Canvas> mapTabCanvas;
	private HashMap<Canvas, LSystemJFX> mapCanvasLSystem;
	private EventHandler<WorkerStateEvent> eventlsystemBuildSuccess;
	private EventHandler<WorkerStateEvent> eventlsystemBuildRunning;
	private EventHandler<WorkerStateEvent> eventlsystemBuildFailed;
	private final int MAX_WIDTH;
	private final int MAX_HEIGHT;
	
	public MainWindowController()
	{
		MAX_WIDTH = 4000;
		MAX_HEIGHT = 4000;
	}
	
	@FXML
	private void initialize()
	{
		mapPausableNodes = new HashSet<>();
		mapTabCanvas = new HashMap<>();
		mapCanvasLSystem = new HashMap<>();
		
		mapPausableNodes.add(buttonCmdDraw);
		mapPausableNodes.add(sliderIterations);
		mapPausableNodes.add(sliderLineWidth);
		mapPausableNodes.add(sliderStartX);
		mapPausableNodes.add(sliderStartY);
		mapPausableNodes.add(tabpane);
		
		mapTabCanvas.put(tabHilbertCurve, canvasHilbertDrawTarget);
		mapCanvasLSystem.put(canvasHilbertDrawTarget, new HilbertCurve());
		
		mapTabCanvas.put(tabKochCurve, canvasKochDrawTarget);
		mapCanvasLSystem.put(canvasKochDrawTarget, new KochCurve());
		
		mapTabCanvas.put(tabKochSnowflake, canvasKochSnowflakeDrawTarget);
		mapCanvasLSystem.put(canvasKochSnowflakeDrawTarget, new KochSnowflakeCurve());
		
		mapTabCanvas.put(tabKochIL, canvasKochILDrawTarget);
		mapCanvasLSystem.put(canvasKochILDrawTarget, new KochIslandLakeCurve());
		
		mapTabCanvas.put(tabKochVariantA, canvasKochVariantADrawTarget);
		mapCanvasLSystem.put(canvasKochVariantADrawTarget, new KochVariantACurve());
		
		mapTabCanvas.put(tabSierpinskiArrow, canvasSierpinskiArrowDrawTarget);
		mapCanvasLSystem.put(canvasSierpinskiArrowDrawTarget, new SierpinskiArrowCurve());
		
		mapTabCanvas.put(tabSierpinskiTriangle, canvasSierpinskiTriangleDrawTarget);
		mapCanvasLSystem.put(canvasSierpinskiTriangleDrawTarget, new SierpinskiTriangleCurve());
		
		mapTabCanvas.put(tabBinaryTree, canvasBinaryTreeDrawTarget);
		mapCanvasLSystem.put(canvasBinaryTreeDrawTarget, new BinaryCurve());
		
		mapTabCanvas.put(tabPlant, canvasPlantDrawTarget);
		mapCanvasLSystem.put(canvasPlantDrawTarget, new PlantCurve());
		
		canvasCurrentDrawTarget = mapTabCanvas.get( tabpane.getTabs().get(0) );
		
		//TODO
//		scrollPaneTest.addEventFilter(ScrollEvent.ANY, e -> {
//			System.out.println(e.getDeltaX());
//		});
//		scrollPaneTest.viewportBoundsProperty().addListener( (a,b,c) -> {
//			System.out.println(a);
//			System.out.println( String.format("old:(%.2f, %.2f), new:(%.2f, %.2f)", b.getCenterX(), b.getCenterY(),c.getCenterX(), c.getCenterY()) );
//		});
		
		tabpane.getSelectionModel().selectedItemProperty().addListener( (obs, oldTab, newTab) -> {
			Canvas oldCanvas = mapTabCanvas.get(oldTab);
			GraphicsContext gctx = oldCanvas.getGraphicsContext2D();
			gctx.clearRect(0, 0, oldCanvas.getWidth(), oldCanvas.getHeight());
			oldCanvas.setWidth(100);
			oldCanvas.setHeight(100);
			
			canvasCurrentDrawTarget = mapTabCanvas.get(newTab);
			canvasCurrentDrawTarget.setVisible(true);
		});
		
		eventlsystemBuildRunning = e -> setNodesDisabled(mapPausableNodes, true);
		eventlsystemBuildFailed = e -> setNodesDisabled(mapPausableNodes, false);
		
		eventlsystemBuildSuccess = e -> {
			setNodesDisabled(mapPausableNodes, false);
			GraphicsContext gctx = canvasCurrentDrawTarget.getGraphicsContext2D();
			gctx.clearRect(0, 0, canvasCurrentDrawTarget.getWidth(), canvasCurrentDrawTarget.getHeight());
			
			try
			{
				for(Line line : (Collection<Line>)e.getSource().getValue())
				{
					gctx.beginPath();
					gctx.moveTo(line.getStartX(), line.getStartY());
					gctx.lineTo(line.getEndX(), line.getEndY());
					
					gctx.setLineWidth(2);
					
					gctx.closePath();
					gctx.stroke();
				}
			}
			catch(ClassCastException ex)
			{
				System.err.println(ex.getMessage());
			}
		};
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
		
		LSystemBuilderTask buildTask = new LSystemBuilderTask();
		buildTask.setOnRunning(eventlsystemBuildRunning);
		buildTask.setOnSucceeded(eventlsystemBuildSuccess);
		buildTask.setOnFailed(eventlsystemBuildFailed);
		buildTask.setIterations(iterations);
		buildTask.setOrigin(lstart);
		buildTask.setLSystem(mapCanvasLSystem.get(canvasCurrentDrawTarget));
		buildTask.setDrawArea(new BoundingBox(canvasCurrentDrawTarget.getLayoutX(), canvasCurrentDrawTarget.getLayoutY(), canvasCurrentDrawTarget.getWidth(), canvasCurrentDrawTarget.getHeight()));
		
		Thread buildThread = new Thread(buildTask);
		buildThread.start();
	}
	
	private void setNodesDisabled(Collection<Node> nodeList, boolean disabled)
	{
		nodeList.forEach(
				node -> node.setDisable(disabled)
		);
	}
}