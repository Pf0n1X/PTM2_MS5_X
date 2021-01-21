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
	
	// Constant Members
	private static final Pattern p = Pattern
			.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	
	// Data Members
	private Client client;
	private MainWindowController mainWindowController;

	// Constructors
	public ConnectWindowController() {
		
	}
	
	// Methods
	@FXML
	public void initialize() {
		setDefualts();
	}
	
	public void onConnectButtonPressed() {
		connect(txtIP.textProperty().get(), Integer.parseInt(txtPort.textProperty().get()));
		mainWindowController.simulatorConnected();
		mainWindowController.closeConnectWindow();	
	}

	private boolean connect(String ip, int port) {
		client.connect(ip, port);
		
		return false;
	}

	
	private boolean ipValidityCheck(String ip) {
		return this.p.matcher(ip).matches();
	}

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
