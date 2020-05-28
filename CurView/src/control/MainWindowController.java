package control;

import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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
}