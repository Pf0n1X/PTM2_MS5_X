package view_model;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
//import view_model.MainWindowViewModel;

public class CalculatePathWindowController {
	
	// Data Members
	@FXML
	TextField txtIP;
	
	@FXML
	TextField txtPort;
	
	private MainWindowController controller;
	
	// Constructors
	public CalculatePathWindowController() {
	
	}
	
	// Methods
	public void initialize() {
		setDefaults();
	}
	
	public void onConnectButtonPressed() {
		this.controller.setPathSolverIP(txtIP.getText());
		this.controller.setPathSolverPort(txtPort.getText());
		this.controller.calculatePath();
		this.controller.closeCalculatePathWindow();
	}
	
	// Getters & Setters
	public MainWindowController getController() {
		return controller;
	}

	public void setMainController(MainWindowController controller) {
		this.controller = controller;
	}
	
	public void setDefaults() {
		this.txtIP.setText("127.0.0.1");
		this.txtPort.setText("1111");
	}
}