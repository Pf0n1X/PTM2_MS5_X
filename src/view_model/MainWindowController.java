package view_model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Observable;
import java.util.Observer;

import clientX.Client;
import clientX.SimpleClient;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import model.PlaneLocationModel;
import view.Main;
import view.Map;
//import view_model.MainWindowViewModel;

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
	
	private File txt;
	private File csv;
//	MainWindowViewModel ViewModel;
	DoubleProperty alieronVal, elevatorVal, flapsval;
	private Stage connectWindow;
	private Stage calculatePathWindow;
	
	String MCL = "Manual Controls locked! - To manualy control the aircraft you need to press the Manual Controls button first";
	String APL = "Cannot Execute! = To use the AutoPilot option you need to press the AutoPilot mode Button first";
	String na = "N/A";
	// Constructors
	public MainWindowController() {
		// TODO Auto-generated constructor stub
		this.interpreter = new interpreterX.Interpreter();
		this.client = new SimpleClient();
		this.planeLocModel = new PlaneLocationModel(this);
		planeLocModel.setClient(client);
//		this.map.paintMap();
		

	}
	
	// Methods
	@FXML
	public void initialize() {
		this.map.setMainWindowController(this);
		sRudder.valueProperty().addListener((obs, prevVal, newVal) -> {
			// TODO: Add code for changing rudder.
			this.client.set("/controls/flight/rudder", newVal.doubleValue());
		});
		
		sThrottle.valueProperty().addListener((obs, prevVal, newVal) -> {
			this.client.set("/controls/engines/current-engine/throttle", newVal.doubleValue());
		});
		
		btnConnect.setOnMouseClicked((event) -> {
			// TODO: Add code for connecting.
		});
		
		btnCalc.setOnMouseClicked((event) -> {
			// TODO: Add code for calculating the path.
			onCalculatePathButtonPressed(event);
		});
		
		btnRunCodeCommands.setOnMouseClicked((event) -> {
		    String[] lines;
			
		    try {
		    	System.out.println("Sending commands");
				lines = Files.lines(txt.toPath()).toArray(String[]::new);
			      interpreter.runSimulator(lines);
			      interpreter.getManager().getParser().Resume();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
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
			// TODO Auto-generated catch block
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
	
	public void onCalculatePathButtonPressed(MouseEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CalculatePathWindow.fxml"));
			AnchorPane window = (AnchorPane) fxmlLoader.load();
			CalculatePathWindowController controller = fxmlLoader.getController();
			controller.setMainController(this);
			Scene scene = new Scene(window);
			this.calculatePathWindow = new Stage();
			this.calculatePathWindow.setScene(scene);
			this.calculatePathWindow.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
}
