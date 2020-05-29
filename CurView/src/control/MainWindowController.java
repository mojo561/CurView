package control;

import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import model.BinaryCurve;
import model.HilbertCurve;

public class MainWindowController
{
	@FXML
	private Button buttonCmdDraw;
	
	@FXML
	private TabPane tabpane;
	
	@FXML
	private Tab tabHilbertCurve;
	
	@FXML
	private Tab tabKochCurve;
	
	@FXML
	private Canvas canvasHilbertDrawTarget;
	
	@FXML
	private Canvas canvasKochDrawTarget;
	
	@FXML
	private Slider sliderStartX;
	
	@FXML
	private Slider sliderStartY;
	
	@FXML
	private Slider sliderIterations;
	
	@FXML
	private Slider sliderLineWidth;
	
	private HashMap<Tab, Canvas> tabCanvasMap;
	
	@FXML
	private void initialize()
	{
		tabCanvasMap = new HashMap<>();
		tabCanvasMap.put(tabHilbertCurve, canvasHilbertDrawTarget);
		tabCanvasMap.put(tabKochCurve, canvasKochDrawTarget);
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