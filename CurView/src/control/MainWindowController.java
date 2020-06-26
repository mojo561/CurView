package control;

import java.util.ArrayList;
import java.util.Collection;
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
import javafx.geometry.Point2D;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import model.BinaryCurve;
import model.ETabTags;
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
	private GridPane rootNode;

	@FXML
	private ScrollPane scrollPaneTest;
	
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
		timerDrawScheduler = new Timer(true);
		mapPausableNodes = new HashSet<>();
		
		canvasCurrentDrawTarget = new Canvas();
		
		scrollPaneTest = new ScrollPane();
		scrollPaneTest.setPannable(true);
		scrollPaneTest.setStyle("-fx-border-color: black");
		
		scrollPaneTest.setContent(canvasCurrentDrawTarget);
		
		ArrayList<Tab> tabList = vaTabBuilder(
				ETabTags.HILBERT,
				ETabTags.KOCH_A,
				ETabTags.KOCH_B,
				ETabTags.KOCH_C,
				ETabTags.KOCH_D,
				ETabTags.BINARY_TREE,
				ETabTags.SIERPINSKI_ARROW,
				ETabTags.SIERPINSKI_TRIANGLE,
				ETabTags.PLANT);
		
		tabList.get(0).setContent(scrollPaneTest);
		
		tabList.forEach(tab -> tabpaneMain.getTabs().add(tab));
		
		mapPausableNodes.add(buttonCmdDraw);
		mapPausableNodes.add(sliderIterations);
		mapPausableNodes.add(sliderLineWidth);
		mapPausableNodes.add(sliderStartX);
		mapPausableNodes.add(sliderStartY);
		mapPausableNodes.add(tabpaneMain);
		
		//TODO:new
//		scrollPaneTest.addEventFilter(ScrollEvent.ANY, e -> {
//			System.out.println(e.getDeltaX());
//		});
//		ChangeListener<Number> foo = new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
//				timerDrawScheduler.schedule(new TimerTask() {
//					
//					private final double foo = arg2.doubleValue();
//					
//					@Override
//					public void run() {
//						if(foo == scrollPaneTest.getVvalue())
//						{
//							System.out.println("vert value changed, drawing...");
//							cmdDraw();
//						}
//					}}, 1000);
//			}
//		};
//		
//		ChangeListener<Number> bar = new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
//				timerDrawScheduler.schedule(new TimerTask() {
//					
//					private final double foo = arg2.doubleValue();
//					
//					@Override
//					public void run() {
//						if(foo == scrollPaneTest.getHvalue())
//						{
//							System.out.println("horz value changed, drawing...");
//							cmdDraw();
//						}
//					}}, 1000);
//			}
//		};
//		
//		scrollPaneTest.vvalueProperty().addListener(foo);
//		scrollPaneTest.hvalueProperty().addListener(bar);
		
		EventHandler<MouseEvent> eventScrollPaneMouseEvent = new EventHandler<MouseEvent>()
		{
			private double vvalue;
			private double hvalue;
			
			@Override
			public void handle(MouseEvent mEvent)
			{
				if(mEvent.getEventType() == MouseEvent.MOUSE_PRESSED)
				{
					vvalue = scrollPaneTest.getVvalue();
					hvalue = scrollPaneTest.getHvalue();
				}
				else if(mEvent.getEventType() == MouseEvent.MOUSE_RELEASED)
				{
					if(vvalue != scrollPaneTest.getVvalue() || hvalue != scrollPaneTest.getHvalue())
					{
						System.out.println("drawing...");
						cmdDraw();
					}
				}
			}
		};
		
		scrollPaneTest.setOnMousePressed(eventScrollPaneMouseEvent);
		scrollPaneTest.setOnMouseReleased(eventScrollPaneMouseEvent);
		
		//TODO: new
		rootNode.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				//TODO: hmmm..
				double newHeight = arg2.doubleValue() + 1000;
				if(newHeight <= MAX_HEIGHT)
				{
					canvasCurrentDrawTarget.setHeight(newHeight);
				}
				else
				{
					canvasCurrentDrawTarget.setHeight(MAX_HEIGHT);
				}
			}});
		rootNode.widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				//TODO: hmmm..
				double newWidth = arg2.doubleValue() + 1000;
				if(newWidth <= MAX_WIDTH)
				{
					canvasCurrentDrawTarget.setWidth(newWidth);
				}
				else
				{
					canvasCurrentDrawTarget.setWidth(MAX_WIDTH);
				}
			}});
		
		tabpaneMain.getSelectionModel().selectedItemProperty().addListener( (obs, oldTab, newTab) -> {
			oldTab.setContent(null);
			newTab.setContent(scrollPaneTest);
			GraphicsContext gctx = canvasCurrentDrawTarget.getGraphicsContext2D();
			gctx.clearRect(0, 0, canvasCurrentDrawTarget.getWidth(), canvasCurrentDrawTarget.getHeight());
		});
		
		eventlsystemBuildRunning = e -> setNodesDisabled(mapPausableNodes, true);
		eventlsystemBuildFailed = e -> setNodesDisabled(mapPausableNodes, false);
		
		eventlsystemBuildSuccess = e -> {
			setNodesDisabled(mapPausableNodes, false);
			GraphicsContext gctx = canvasCurrentDrawTarget.getGraphicsContext2D();
			gctx.clearRect(0, 0, canvasCurrentDrawTarget.getWidth(), canvasCurrentDrawTarget.getHeight());
			
			try
			{
				//TODO: debug
				int entityCount = 0;
				for(Line line : (Collection<Line>)e.getSource().getValue())
				{
					gctx.beginPath();
					gctx.moveTo(line.getStartX(), line.getStartY());
					gctx.lineTo(line.getEndX(), line.getEndY());
					
					gctx.setLineWidth(2);
					
					gctx.closePath();
					gctx.stroke();
					//TODO: debug
					++entityCount;
				}
				//TODO: debug
				System.out.println( String.format("drew %d line nodes", entityCount) );
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
		
		int lineLength = (int)sliderLineWidth.getValue();
		int iterations = (int)sliderIterations.getValue();
		
		//TODO: until we add something to the GUI in the future, we can control the starting rotation of the resulting curve here
		Point2D pstart;
		Line lstart;
		
		pstart = new Point2D(lineLength, 0);
			
		lstart = new Line(sliderStartX.getValue(), sliderStartY.getValue(), sliderStartX.getValue() + pstart.getX(), sliderStartY.getValue() + pstart.getY());
		double offsetVert = 0;
		double offsetHorz = 0;
		
		//credit: https://stackoverflow.com/a/40682080
		//TODO: since we're now dealing with the scrollbar in the controller, is there any way we can add our own ScrollBars instead of relying on this lookupAll method?
		for(Node node : scrollPaneTest.lookupAll(".scroll-bar"))
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
		buildTask.setOrigin(lstart);
		
		tabpaneMain.getTabs().forEach(tab -> {
			if(tab.isSelected())
			{
				switch(ETabTags.valueOf(tab.getId()))
				{
				case BINARY_TREE:
					buildTask.setLSystem(new BinaryCurve());
					break;
				case HILBERT:
					buildTask.setLSystem(new HilbertCurve());
					break;
				case KOCH_A:
					buildTask.setLSystem(new KochCurve());
					break;
				case KOCH_B:
					buildTask.setLSystem(new KochSnowflakeCurve());
					break;
				case KOCH_C:
					buildTask.setLSystem(new KochIslandLakeCurve());
					break;
				case KOCH_D:
					buildTask.setLSystem(new KochVariantACurve());
					break;
				case PLANT:
					buildTask.setLSystem(new PlantCurve());
					break;
				case SIERPINSKI_ARROW:
					buildTask.setLSystem(new SierpinskiArrowCurve());
					break;
				case SIERPINSKI_TRIANGLE:
					buildTask.setLSystem(new SierpinskiTriangleCurve());
					break;
				default:
					buildTask.setLSystem(null);
					break;
				}
				return;
			}
		});

		buildTask.setDrawArea(new BoundingBox(offsetHorz, offsetVert, scrollPaneTest.getWidth(), scrollPaneTest.getHeight()));
		
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