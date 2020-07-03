package control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import model.BinaryCurve;
import model.ETabTags;
import model.HilbertCurve;
import model.KochCurve;
import model.KochIslandLakeCurve;
import model.KochSnowflakeCurve;
import model.KochVariantACurve;
import model.KochVariantBCurve;
import model.LSystemBuilderTask;
import model.PlantCurve;
import model.SierpinskiArrowCurve;
import model.SierpinskiTriangleCurve;

public class MainWindowController
{
	@FXML
	private GridPane rootNode;
	
	@FXML
	private Button buttonCmdDraw;
	
	@FXML
	private TabPane tabpaneMain;

	@FXML
	private Slider sliderStartX;
	
	@FXML
	private Slider sliderStartY;
	
	@FXML
	private Slider sliderIterations;
	
	@FXML
	private Slider sliderLineWidth;
	
	private Timer timerDrawScheduler;
	private ScrollPane scrollPaneCanvasContainer;
	private Canvas canvasCurrentDrawTarget;
	private HashSet<Node> mapPausableNodes;
	private EventHandler<WorkerStateEvent> eventlsystemBuildSuccess;
	private EventHandler<WorkerStateEvent> eventlsystemBuildRunning;
	private EventHandler<WorkerStateEvent> eventlsystemBuildFailed;
	private final int MAX_WIDTH;
	private final int MAX_HEIGHT;
	
	public MainWindowController()
	{
		MAX_WIDTH = 4000;
		MAX_HEIGHT = 4000;
		timerDrawScheduler = new Timer(true);
		mapPausableNodes = new HashSet<>();
		
		canvasCurrentDrawTarget = new Canvas();
		canvasCurrentDrawTarget.setWidth(MAX_WIDTH);
		canvasCurrentDrawTarget.setHeight(MAX_HEIGHT);
		
		scrollPaneCanvasContainer = new ScrollPane();
		scrollPaneCanvasContainer.setPannable(true);
		scrollPaneCanvasContainer.setStyle("-fx-border-color: black");
		scrollPaneCanvasContainer.setContent(canvasCurrentDrawTarget);
	}
	
	private ArrayList<Tab> vaTabBuilder(ETabTags ...tagIDs)
	{
		ArrayList<Tab> rval = new ArrayList<>();
		
		if(tagIDs == null)
		{
			return rval;
		}
		if(tagIDs.length == 0)
		{
			return rval;
		}
		
		for(ETabTags tag : tagIDs)
		{
			Tab tab = new Tab();
			tab.setId(tag.name());
			tab.setText(tag.toString());
			rval.add(tab);
		}
		return rval;
	}

	@FXML
	private void initialize()
	{
		EventHandler<MouseEvent> eventScrollPaneMouseEvent = new EventHandler<MouseEvent>()
		{
			private double vvalue;
			private double hvalue;
			
			@Override
			public void handle(MouseEvent mEvent)
			{
				if(mEvent.getEventType() == MouseEvent.MOUSE_PRESSED)
				{
					vvalue = scrollPaneCanvasContainer.getVvalue();
					hvalue = scrollPaneCanvasContainer.getHvalue();
				}
				else if(mEvent.getEventType() == MouseEvent.MOUSE_RELEASED)
				{
					if(vvalue != scrollPaneCanvasContainer.getVvalue() || hvalue != scrollPaneCanvasContainer.getHvalue())
					{
						vvalue = scrollPaneCanvasContainer.getVvalue();
						hvalue = scrollPaneCanvasContainer.getHvalue();
						cmdDraw();
					}
				}
			}
		};

		ChangeListener<Number> chglstnrScrollBarEventAttacher = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

				for(Node node : scrollPaneCanvasContainer.lookupAll(".scroll-bar"))
				{
					if(node instanceof ScrollBar)
					{
						node.addEventHandler(MouseEvent.MOUSE_PRESSED, eventScrollPaneMouseEvent);
						node.addEventHandler(MouseEvent.MOUSE_RELEASED, eventScrollPaneMouseEvent);
					}
				}
				scrollPaneCanvasContainer.hvalueProperty().removeListener(this);
				scrollPaneCanvasContainer.vvalueProperty().removeListener(this);
			}
		};
		
		ChangeListener<Number> lsnrRootNodeSize = (obsValue, oldValue, newValue) -> {
		TimerTask ttask = new TimerTask() {
			private double oldHValue = rootNode.getHeight();
			private double oldWValue = rootNode.getWidth();
			@Override
			public void run() {
				double currentHValue = rootNode.getHeight();
				double currentWValue = rootNode.getWidth();
				if(oldHValue == currentHValue && oldWValue == currentWValue)
				{
					cmdDraw();
				}
			}
		};
		timerDrawScheduler.schedule(ttask, 1000);

	};
		
		ChangeListener<Tab> lsnrTabSelected = (obs, oldTab, newTab) -> {
			if(oldTab != null)
			{
				oldTab.setContent(null);
			}
			newTab.setContent(scrollPaneCanvasContainer);
			GraphicsContext gctx = canvasCurrentDrawTarget.getGraphicsContext2D();
			gctx.clearRect(0, 0, canvasCurrentDrawTarget.getWidth(), canvasCurrentDrawTarget.getHeight());
			scrollPaneCanvasContainer.setVvalue(0);
			scrollPaneCanvasContainer.setHvalue(0);
		};
		
		eventlsystemBuildRunning = e -> setNodesDisabled(mapPausableNodes, true);
		eventlsystemBuildFailed = e -> setNodesDisabled(mapPausableNodes, false);
		eventlsystemBuildSuccess = e -> {
			setNodesDisabled(mapPausableNodes, false);
			GraphicsContext gctx = canvasCurrentDrawTarget.getGraphicsContext2D();
			gctx.clearRect(0, 0, canvasCurrentDrawTarget.getWidth(), canvasCurrentDrawTarget.getHeight());
			
			Collection<Line> eventLineResults = Collections.<Line>emptyList();
			Object eventSourceValue = e.getSource().getValue();
			
			if(eventSourceValue instanceof Collection<?>)
			{
				try
				{
					eventLineResults = (Collection<Line>)eventSourceValue;
				}
				catch(ClassCastException ex)
				{
					System.err.println(ex.getMessage());
				}
			}
			
			for(Line line : eventLineResults)
			{
				gctx.beginPath();
				gctx.moveTo(line.getStartX(), line.getStartY());
				gctx.lineTo(line.getEndX(), line.getEndY());
				
				//TODO: trying to make the drawn lines more distinct...
				//gctx.setLineWidth(2);

				gctx.closePath();
				gctx.stroke();
			}
		};
		
		scrollPaneCanvasContainer.setOnMousePressed(eventScrollPaneMouseEvent);
		scrollPaneCanvasContainer.setOnMouseReleased(eventScrollPaneMouseEvent);
		scrollPaneCanvasContainer.hvalueProperty().addListener(chglstnrScrollBarEventAttacher);
		scrollPaneCanvasContainer.vvalueProperty().addListener(chglstnrScrollBarEventAttacher);
		
		rootNode.widthProperty().addListener(lsnrRootNodeSize);
		rootNode.heightProperty().addListener(lsnrRootNodeSize);
		
		tabpaneMain.getSelectionModel().selectedItemProperty().addListener(lsnrTabSelected);
		
		ArrayList<Tab> tabList = vaTabBuilder(
				ETabTags.HILBERT,
				ETabTags.KOCH_A,
				ETabTags.KOCH_B,
				ETabTags.KOCH_C,
				ETabTags.KOCH_D,
				ETabTags.KOCH_E,
				ETabTags.BINARY_TREE,
				ETabTags.SIERPINSKI_ARROW,
				ETabTags.SIERPINSKI_TRIANGLE,
				ETabTags.PLANT);
		
		tabList.get(0).setContent(scrollPaneCanvasContainer);
		
		tabList.forEach(tab -> tabpaneMain.getTabs().add(tab));
		
		mapPausableNodes.add(buttonCmdDraw);
		mapPausableNodes.add(sliderIterations);
		mapPausableNodes.add(sliderLineWidth);
		mapPausableNodes.add(sliderStartX);
		mapPausableNodes.add(sliderStartY);
		mapPausableNodes.add(tabpaneMain);
	}
	
	@FXML
	private void cmdDraw()
	{
		if(canvasCurrentDrawTarget == null)
		{
			return;
		}
		
		int lineLength = (int)sliderLineWidth.getValue();
		int iterations = (int)sliderIterations.getValue();
		
		double offsetVert = 0;
		double offsetHorz = 0;
		
		//credit: https://stackoverflow.com/a/40682080
		//TODO: since we're now dealing with the scrollbar in the controller, is there any way we can add our own ScrollBars instead of relying on this lookupAll method?
		for(Node node : scrollPaneCanvasContainer.lookupAll(".scroll-bar"))
		{
			if(node instanceof ScrollBar)
			{
				ScrollBar sb = (ScrollBar)node;
				if(sb.getOrientation() == Orientation.HORIZONTAL)
				{
					double hbarMinPosition =  (canvasCurrentDrawTarget.getWidth() - sb.getWidth()) * sb.getValue();
					offsetHorz = hbarMinPosition;
				}
				else if(sb.getOrientation() == Orientation.VERTICAL)
				{
					double vbarMinPosition =  (canvasCurrentDrawTarget.getHeight() - sb.getHeight()) * sb.getValue();
					offsetVert = vbarMinPosition;
				}
			}
		}
		
		LSystemBuilderTask buildTask = new LSystemBuilderTask();
		buildTask.setOnRunning(eventlsystemBuildRunning);
		buildTask.setOnSucceeded(eventlsystemBuildSuccess);
		buildTask.setOnFailed(eventlsystemBuildFailed);
		buildTask.setIterations(iterations);
		
		tabpaneMain.getTabs().forEach(tab -> {
			if(tab.isSelected())
			{
				//TODO: until we add something to the GUI in the future, starting rotation of the resulting curve is set here as well
				switch(ETabTags.valueOf(tab.getId()))
				{
				case BINARY_TREE:
					buildTask.setOrigin(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue() + lineLength, sliderStartY.getValue()));
					buildTask.setLSystem(new BinaryCurve());
					break;
				case HILBERT:
					buildTask.setOrigin(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue(), sliderStartY.getValue() + lineLength));
					buildTask.setLSystem(new HilbertCurve());
					break;
				case KOCH_A:
					buildTask.setOrigin(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue() + lineLength, sliderStartY.getValue()));
					buildTask.setLSystem(new KochCurve());
					break;
				case KOCH_B:
					buildTask.setOrigin(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue() + lineLength, sliderStartY.getValue()));
					buildTask.setLSystem(new KochSnowflakeCurve());
					break;
				case KOCH_C:
					buildTask.setOrigin(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue() + lineLength, sliderStartY.getValue()));
					buildTask.setLSystem(new KochIslandLakeCurve());
					break;
				case KOCH_D:
					buildTask.setOrigin(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue(), sliderStartY.getValue() + lineLength));
					buildTask.setLSystem(new KochVariantACurve());
					break;
				case KOCH_E:
					buildTask.setOrigin(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue() + lineLength, sliderStartY.getValue()));
					buildTask.setLSystem(new KochVariantBCurve());
					break;
				case PLANT:
					buildTask.setOrigin(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue() + lineLength, sliderStartY.getValue()));
					buildTask.setLSystem(new PlantCurve());
					break;
				case SIERPINSKI_ARROW:
					buildTask.setOrigin(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue(), sliderStartY.getValue() + lineLength));
					buildTask.setLSystem(new SierpinskiArrowCurve());
					break;
				case SIERPINSKI_TRIANGLE:
					buildTask.setOrigin(new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue(), sliderStartY.getValue() + lineLength));
					buildTask.setLSystem(new SierpinskiTriangleCurve());
					break;
				default:
					buildTask.setLSystem(null);
					break;
				}
				return;
			}
		});

		buildTask.setDrawArea(new BoundingBox(offsetHorz, offsetVert, scrollPaneCanvasContainer.getWidth(), scrollPaneCanvasContainer.getHeight()));
		
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