package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import view_model.MainWindowController;

public class PathSolverModel {

	// Data Members
	private MainWindowController controller;
	private String ip;
	private String port;
	private double[][] heightMap;
	private int srcX;
	private int srcY;
	private int destX;
	private int destY;
	private int[][] path;
	private boolean serverConnected;

	// Constructors
	public PathSolverModel(MainWindowController mainWindowController) {
		this.controller = controller;
	}

	// Methods
	public void calculateShortestPath() {
		try {
//			Socket theServer = new Socket("127.0.0.1", 1111);
			Socket theServer = new Socket(this.ip, Integer.parseInt(this.port));
			PrintWriter writer = new PrintWriter(theServer.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(theServer.getInputStream()));

			String[] newMat = new String[heightMap.length];

			for (int i = 0; i < newMat.length; i++) {
				newMat[i] = "";
			}

			for (int i = 0; i < this.heightMap.length; i++) {
				for (int j = 0; j < this.heightMap[i].length; j++) {
					if (j == this.heightMap[i].length - 1) {
						newMat[i] = newMat[i].concat((int) this.heightMap[i][j] + "");
					} else {
						newMat[i] = newMat[i].concat((int) this.heightMap[i][j] + ",");
					}
				}
			}

			writer.println(heightMap.length + "," + heightMap[0].length);

			// Print the maze
			for (String line : newMat) {
				writer.println(line);
			}

			writer.println("end");

			writer.println(srcY + "," + srcX);

			// Finish point in maze.
			writer.println(destY + "," + destX);

			writer.flush();
			String responseLine;
			responseLine = reader.readLine();

			System.out.println("Get response");
			System.out.println(responseLine);

			this.setPath(buildPathFromResponse(responseLine));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread t = new Thread();
		try {
			t.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int[][] buildPathFromResponse(String response) {
		String[] path = response.split(",");
		int[][] result = new int[path.length + 1][2];

		result[0][0] = srcX;
		result[0][1] = srcY;
		for (int i = 1; i <= path.length; i++) {
			switch (path[i - 1]) {
			case "Up":
				result[i][0] = result[i - 1][0] - 1;
				result[i][1] = result[i - 1][1];
				break;
			case "Down":
				result[i][0] = result[i - 1][0] + 1;
				result[i][1] = result[i - 1][1];
				break;
			case "Left":
				result[i][0] = result[i - 1][0];
				result[i][1] = result[i - 1][1] - 1;
				break;
			case "Right":
				result[i][0] = result[i - 1][0];
				result[i][1] = result[i - 1][1] + 1;
				break;
			}
		}

		return result;
	}

	// Getters & Setters
	public int getSrcX() {
		return srcX;
	}

	public void setSrcX(int srcX) {
		this.srcX = srcX;
	}

	public int getSrcY() {
		return srcY;
	}

	public void setSrcY(int srcY) {
		this.srcY = srcY;
	}

	public int getDestX() {
		return destX;
	}

	public void setDestX(int destX) {
		this.destX = destX;
	}

	public int getDestY() {
		return destY;
	}

	public void setDestY(int destY) {
		this.destY = destY;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public String getIP() {
		return this.ip;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPort() {
		return this.port;
	}

	public void setHeightMap(double[][] heightMap) {
		this.heightMap = heightMap;
	}

	public double[][] getHeightMap() {
		return this.heightMap;
	}

	public int[][] getPath() {
		return path;
	}

	public void setPath(int[][] path) {
		this.path = path;
	}

	public boolean isServerConnected() {
		return serverConnected;
	}

	public void setServerConnected(boolean serverConnected) {
		this.serverConnected = serverConnected;
	}
}
