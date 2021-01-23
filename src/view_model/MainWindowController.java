package view_model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Observable;
import java.util.Observer;
import clientX.Client;
import clientX.DefaultClient;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.PathSolverModel;
import model.PlaneLocationModel;
import view.Main;
import view.Map;

public class MainWindowController implements Observer {

	@FXML
	Button btnCalc, btnConnect, btnRunCodeCommands;
	@FXML
	RadioButton radioBtnManual, radioBtnAutopilot;
	@FXML
	Label statlabel, airspeed, altitude;
	@FXML
	TextArea txtCommands;
	@FXML
	Circle circleJoystick, circleJoystickBorder;
	@FXML
	Slider sRudder, sThrottle;
	@FXML
	Map map;

	// Data Members
	interpreterX.Interpreter interpreter;
	Client client;
	private PlaneLocationModel planeLocModel;
	private PathSolverModel pathSolverModel;

	private File txt;
	private File csv;
//	MainWindowViewModel ViewModel;
	DoubleProperty alieronVal, elevatorVal, flapsval;
	private Stage connectWindow;
	private Stage calculatePathWindow;

	// Manual Mode variables
	private double radius = 0;
	private double xCenter = 0;
	private double yCenter = 0;
	private double initializedXCenter = 0;
	private double initializedYCenter = 0;

	String MCL = "Manual Controls locked! - To manualy control the aircraft you need to press the Manual Controls button first";
	String APL = "Cannot Execute! = To use the AutoPilot option you need to press the AutoPilot mode Button first";
	String na = "N/A";

	// Constructors
	public MainWindowController() {
		// TODO Auto-generated constructor stub
		this.interpreter = new interpreterX.Interpreter();
		this.client = new DefaultClient();
		this.planeLocModel = new PlaneLocationModel(this);
		this.pathSolverModel = new PathSolverModel(this);
		planeLocModel.setClient(client);
	}

	// Methods
	@FXML
	public void initialize() {
		this.map.setMainWindowController(this);
		initializedXCenter = circleJoystick.getLayoutX();
		initializedYCenter = circleJoystick.getLayoutY();
		sRudder.valueProperty().addListener((obs, prevVal, newVal) -> {
			if (this.radioBtnManual.isSelected()) {

				this.client.set("/controls/flight/rudder", newVal.doubleValue());
			} else {
				this.sRudder.setValue(0.0);
			}
		});

		sThrottle.valueProperty().addListener((obs, prevVal, newVal) -> {
			if (this.radioBtnManual.isSelected()) {

				this.client.set("/controls/engines/current-engine/throttle", newVal.doubleValue());
			} else {
				this.sThrottle.setValue(0.0);
			}
		});

		btnConnect.setOnMouseClicked((event) -> {
		});

		btnCalc.setOnMouseClicked((event) -> {
		});

		btnRunCodeCommands.setOnMouseClicked((event) -> {
			String[] lines = this.txtCommands.getText().split("\n");
			interpreter.runSimulator(lines);
			interpreter.getManager().getParser().Resume();
		});
	}

	@Override
	public void update(Observable o, Object arg) {

	}

	public void onMapClicked(MouseEvent event) {
		this.map.setDestination(event.getX() / this.map.getSqrWidth(), event.getY() / this.map.getSqrHeight());
//		TODO: Add this if server is connected RON.
		if (this.pathSolverModel.isServerConnected()) {
			calculatePath();
			int[][] path = this.pathSolverModel.getPath();
			this.map.setPath(path);
		}
		this.map.paintAll();
	}

	public void onConnectButtonPressed() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ConnectWindow.fxml"));
			AnchorPane window = (AnchorPane) fxmlLoader.load();
			ConnectWindowController controller = fxmlLoader.getController();
			controller.setMainWindowController(this);
			controller.setClient(this.client);
			Scene scene = new Scene(window);
			this.connectWindow = new Stage();
			connectWindow.setScene(scene);
			connectWindow.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnectWindow() {
		connectWindow.close();
	}

	public void loadData() {
		this.csv = fileLoader();
		if (this.csv != null) {
			this.map.setCsv(this.csv);
			this.map.paintMap();
		}
	}

	public void toggleManualPilot() {
		if (this.radioBtnAutopilot.isSelected()) {
			this.radioBtnAutopilot.setSelected(false);

		}
		this.radioBtnManual.setSelected(true);
		this.statlabel.setText("Manual mode is ON");
	}

	public void toggleAutoPilot() {
		if (this.radioBtnManual.isSelected()) {
			this.radioBtnManual.setSelected(false);

		}
		this.radioBtnAutopilot.setSelected(true);
		this.statlabel.setText("AutoPilot mode is ON");
	}

	public void onCalculatePathButtonPressed() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CalculatePathWindow.fxml"));
			AnchorPane window = (AnchorPane) fxmlLoader.load();
			CalculatePathWindowController controller = fxmlLoader.getController();
			controller.setMainController(this);
			Scene scene = new Scene(window);
			this.calculatePathWindow = new Stage();
			this.calculatePathWindow.setScene(scene);
			this.calculatePathWindow.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeCalculatePathWindow() {
		if (this.calculatePathWindow != null) {
			this.calculatePathWindow.close();
			this.calculatePathWindow = null;
		}
	}

	public void loadTextFile() throws IOException {
		this.txt = this.fileLoader();

		if (this.txt != null) {
			BufferedReader br = new BufferedReader(new FileReader(this.txt));
			StringBuilder sb = new StringBuilder();
			String st;
			while ((st = br.readLine()) != null) {
				sb.append(st + "\n");
			}
			this.txtCommands.setText(sb.toString());
			br.close();
			this.statlabel.setText("File Loaded Successfuly");
		} else
			this.statlabel.setText("Error with text file, Try Again");
	}

	public File fileLoader() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Select Text File:");
		fc.setInitialDirectory(new File("./src/resources/"));
		File chosen = fc.showOpenDialog(null);
		if (chosen == null)
			return null;
		return chosen;
	}

	public void resetThrottle() {
		if (this.radioBtnManual.isSelected()) {
			this.sThrottle.setValue(0.0);
			this.client.set("/controls/engines/current-engine/throttle", 0.0);
		} else
			this.statlabel.setText(MCL);
	}

	public void resetRudder() {
		if (this.radioBtnManual.isSelected()) {
			this.sRudder.setValue(0.0);
			this.client.set("/controls/flight/rudder", 0.0);
		} else
			this.statlabel.setText(MCL);
	}

	public void setInitY(double initY) {
		this.planeLocModel.setInitY(initY);
	}

	public void setInitX(double initX) {
		this.planeLocModel.setInitX(initX);
	}

	public void setKMPerBlock(double kmPerBlock) {
		this.planeLocModel.setDistance(kmPerBlock);
	}

	public void simulatorConnected() {
		this.planeLocModel.run();
	}

	public void setPlaneCoordinates(double x, double y) {
		this.map.setSrcX(x);
		this.map.setSrcY(y);
		this.map.paintAll();
	}

	public void setAltitude(Double alt) {
		this.altitude.setText(alt + "");
	}

	public void setSpeed(Double speed) {
		this.airspeed.setText(speed + "");
	}

	public void setPathSolverIP(String ip) {
		this.pathSolverModel.setIP(ip);
	}

	public void setPathSolverPort(String port) {
		this.pathSolverModel.setPort(port);
	}

	public void calculatePath() {
		this.pathSolverModel.setSrcX((int) this.map.getSrcX());
		this.pathSolverModel.setSrcY((int) this.map.getSrcY());
		this.pathSolverModel.setDestX((int) this.map.getDestX());
		this.pathSolverModel.setDestY((int) this.map.getDestY());
		this.pathSolverModel.setHeightMap(this.map.getMatrix());
		this.pathSolverModel.calculateShortestPath();
		int[][] path = this.pathSolverModel.getPath();
		this.map.setPath(path);
		this.map.paintAll();
	}

	@FXML
	public void onJoystickRelease(MouseEvent event) {

		circleJoystick.setCenterX(0);
		circleJoystick.setCenterY(0);
	}

	private double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	@FXML
	public void onJoystickDrag(MouseEvent event) {
		if (this.radioBtnManual.isSelected()) {
			if (event.getX() <= 100 && event.getX() >= -100)
				circleJoystick.setCenterX(event.getX());
			circleJoystick.setCenterY(event.getY());

			if (radius == 0) {
				radius = circleJoystickBorder.getRadius();
				xCenter = (circleJoystick.localToScene(circleJoystick.getBoundsInLocal()).getMinX()
						+ circleJoystick.localToScene(circleJoystick.getBoundsInLocal()).getMaxX()) / 2;
				yCenter = (circleJoystick.localToScene(circleJoystick.getBoundsInLocal()).getMinY()
						+ circleJoystick.localToScene(circleJoystick.getBoundsInLocal()).getMaxY()) / 2;
			}

			double x1 = event.getSceneX();
			double y1 = event.getSceneY();
			double x2, y2;
			final int div = 2;
			double distance = distance(event.getSceneX(), event.getSceneY(), xCenter, yCenter);
			if (distance <= radius / div) {

				x2 = x1;
				y2 = y1;
			} else {
				if (x1 > xCenter) {
					double alfa = Math.atan((y1 - yCenter) / (x1 - xCenter));
					double w = radius * Math.cos(alfa);
					double z = radius * Math.sin(alfa);

					x2 = xCenter + w / div;
					y2 = yCenter + z / div;
				} else {
					double alfa = Math.atan((yCenter - y1) / (xCenter - x1));
					double w = radius * Math.cos(alfa);
					double z = radius * Math.sin(alfa);
					x2 = xCenter - w / div;
					y2 = yCenter - z / div;
				}
			}
			client.set("/controls/flight/aileron", (x2 - xCenter) / radius);
			client.set("/controls/flight/elevator", (yCenter - y2) / radius);
		} else
			this.statlabel.setText(MCL);

	}

	public void setServerConnected() {
		this.pathSolverModel.setServerConnected(true);
	}
}
