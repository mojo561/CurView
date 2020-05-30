package control;

import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import model.BinaryCurve;
import model.HilbertCurve;
import model.LSystemJFX;

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
	
	/*****************
	 * CANVAS
	 *****************/
	@FXML
	private Canvas canvasHilbertDrawTarget;
	
	@FXML
	private Canvas canvasKochDrawTarget;
	
	@FXML
	private Canvas canvasKochSnowflakeDrawTarget;
	
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
	
	@FXML
	private void initialize()
	{
		tabCanvasMap = new HashMap<>();
		canvasLSystemMap = new HashMap<>();
		
		tabCanvasMap.put(tabHilbertCurve, canvasHilbertDrawTarget);
		tabCanvasMap.put(tabKochCurve, canvasKochDrawTarget);
		tabCanvasMap.put(tabKochSnowflake, canvasKochSnowflakeDrawTarget);
		
		canvasLSystemMap.put(canvasHilbertDrawTarget, new HilbertCurve());

		tabpane.getSelectionModel().selectedItemProperty().addListener( (obs, oldTab, newTab) -> {
			Canvas oldCanvas = tabCanvasMap.get(oldTab);
			GraphicsContext gctx = oldCanvas.getGraphicsContext2D();
			gctx.clearRect(0, 0, oldCanvas.getWidth(), oldCanvas.getHeight());
			oldCanvas.setWidth(100);
			oldCanvas.setHeight(100);
		});
	}
	
	@FXML
	private void cmdDraw()
	{
		canvasHilbertDrawTarget.setWidth(8192);
		canvasHilbertDrawTarget.setHeight(8192);
		
		GraphicsContext gctx = canvasHilbertDrawTarget.getGraphicsContext2D();
		
		HilbertCurve hc = new HilbertCurve();
		//BinaryCurve bc = new BinaryCurve();
		
		int lineLength = (int)sliderLineWidth.getValue();
		int iterations = (int)sliderIterations.getValue();
		
		gctx.clearRect(0, 0, canvasHilbertDrawTarget.getWidth(), canvasHilbertDrawTarget.getHeight());
		
		for(Line line : hc.build(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue(), sliderStartY.getValue() + lineLength), iterations))
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