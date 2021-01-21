package model;

import clientX.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import view_model.MainWindowController;

public class PlaneLocationModel {
	
	// Data Members
	private Client client;
	private double initX;
	private double initY;
	private double curX;
	private double curY;
    private double distance;
    private MainWindowController controller;
	
	// Constructors
	public PlaneLocationModel(MainWindowController controller) {
		this.controller = controller;
	}
	
	// Methods
	public void run() {
		Timeline fourSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(4),
				new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						double x, y;
						
						Double lat = client.getValue("/position/latitude-deg");
		                Double lon = client.getValue("/position/longitude-deg");
		                Double alt = client.getValue("/instrumentation/altimeter/indicated-altitude-ft");
		                Double heading = client.getValue("/instrumentation/heading-indicator/indicated-heading-deg");
		                Double speed = client.getValue("/velocities/airspeed-kt");
		                
		                x = (lon - initX + distance) / distance;
		                y = (lat - initY + distance) / distance;
		                y *= -1;
		                
		                System.out.println("X: " + x + " Y: " + y);
		                
		                setCoordinates(x, y);
		                setAltitude(alt);
		                setSpeed(speed);
					}
		}));
		
		fourSecondsWonder.setCycleCount(Timeline.INDEFINITE);
		fourSecondsWonder.play();
	}

	protected void setSpeed(Double speed) {
		this.controller.setSpeed(speed);
	}

	protected void setAltitude(Double alt) {
		this.controller.setAltitude(alt);
	}

	private void setCoordinates(double x, double y) {
		this.curX = x;
		this.curY = y;
		
		this.controller.setPlaneCoordinates(x, y);
	}

	// Getters & Setters
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public double getInitX() {
		return initX;
	}

	public void setInitX(double initX) {
		this.initX = initX;
	}

	public double getInitY() {
		return initY;
	}

	public void setInitY(double initY) {
		this.initY = initY;
	}

	public double getCurX() {
		return curX;
	}

	public void setCurX(double curX) {
		this.curX = curX;
	}

	public double getCurY() {
		return curY;
	}

	public void setCurY(double curY) {
		this.curY = curY;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public MainWindowController getController() {
		return controller;
	}

	public void setController(MainWindowController controller) {
		this.controller = controller;
	}
}