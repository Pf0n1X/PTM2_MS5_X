package view_model;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import clientX.Client;
import interpreterX.Interpreter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectWindowController implements Observer  {
	
	@FXML
	Button btnConnect;
	@FXML
	TextField txtIP, txtPort;
	@FXML
	Label lblStatus;
	
	// Data Members
//	private MainWindowController mainWindow = null;
	private final Pattern p = Pattern
			.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	private Client client;
	private MainWindowController mainWindowController;

	// Constructors
	public ConnectWindowController() {
//		this.ip = new SimpleStringProperty();
//		this.port = new SimpleStringProperty();
//		txtPort.setOnC
//		Bindings.bindBidirectional(txtIP.textProperty(), null);
	}
//	public void setViewModel(MainWindowViewModel viewModel) {
//		this.viewModel = viewModel;
//	}
	
	// Methods
	@FXML
	public void initialize() {
		setDefualts();
	}
	
	public void onConnectButtonPressed() {
		connect(txtIP.textProperty().get(), Integer.parseInt(txtPort.textProperty().get()));
		mainWindowController.simulatorConnected();
		
//		if (ipValidityCheck(this.txtIP.getText())) {
//			this.ip = txtIP.getText();
//			try {
//				this.port = Integer.parseInt(this.txtPort.getText());
//			} catch (Exception e) {
//				this.lblStatus.setText("Please enter a valid port");
//				
//				return;
//			}
//			if(connect(this.ip, this.port)) {
//				this.viewModel.setConnected(true);
//				this.mainWindow.closeConnectWindow();	
//			}
//			else lblStatus.setText("Connect Error");
//		} else {
//			lblStatus.setText("Please enter IP and port");
//		}
//		
	}

	
	private boolean connect(String ip, int port) {
		client.connect(ip, port);
		System.out.println("Connected");
//		this.ip = ip;
//		this.port = telnetPort;
//		if(this.viewModel != null) {
//			this.viewModel.resetInterpreter();
//			
////		    Set the rate to 4 and the port to * TODO *
//			this.viewModel.sendCommandToInterpreter("openDataServer " + 5400 + " " + 10);
//			this.viewModel.sendCommandToInterpreter("connect "+ ip + " " + telnetPort);
//			this.viewModel.putSymbolsInSymbolTable();
//			
////			Set the ip and port as the user has entered
//
//			this.viewModel.sendCommandToInterpreter("var throttle = bind \"/controls/engines/current-engine/throttle\"");
//			this.viewModel.sendCommandToInterpreter("var rudder = bind \"/controls/flight/rudder\"");
//			this.viewModel.sendCommandToInterpreter("var aileron = bind \"/controls/flight/aileron\"");
//			this.viewModel.sendCommandToInterpreter("var elevator = bind \"/controls/flight/elevator\"");
//			this.viewModel.sendCommandToInterpreter("var airspeed = bind \"/instrumentation/airspeed-indicator/indicated-speed-kt\"");
//			this.viewModel.sendCommandToInterpreter("var alt = bind \"/instrumentation/altimeter/indicated-altitude-ft\"");
//			this.viewModel.sendCommandToInterpreter("var pitch = bind \"/instrumentation/attitude-indicator/internal-pitch-deg\"");
//			this.viewModel.sendCommandToInterpreter("var roll = bind \"/instrumentation/attitude-indicator/indicated-roll-deg\"");
//			this.viewModel.sendCommandToInterpreter("var offset = bind \"/instrumentation/heading-indicator/offset-deg\"");
//			this.viewModel.sendCommandToInterpreter("var breaks = bind \"/controls/flight/speedbrake\"");
//			this.viewModel.sendCommandToInterpreter("var heading = bind \"/instrumentation/heading-indicator/indicated-heading-deg\"");
//
//			return true;
//		}
//		return false;
		return false;
	}

	
	private boolean ipValidityCheck(String ip) {
		return this.p.matcher(ip).matches();
	}

//	public void setMainWindow(MainWindowController mainWindow) {
//		this.mainWindow = mainWindow;
//	}

	public String getIp() {
		return this.txtIP.textProperty().get();
	}

	public void setDefualts() {
		this.txtIP.setText("127.0.0.1");
		this.txtPort.setText("5402");
	}
	public void clearTextandValues() {
		this.txtIP.clear();
		this.txtPort.clear();
	}	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	public int getPort() {
		return Integer.parseInt(this.txtPort.textProperty().get());
	}

	public void setPort(int port) {
		this.txtPort.textProperty().set(port + "");
	}
	
	public Client getClient() {
		return this.client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}

	public MainWindowController getMainWindowController() {
		return mainWindowController;
	}

	public void setMainWindowController(MainWindowController mainWindowController) {
		this.mainWindowController = mainWindowController;
	}
}
