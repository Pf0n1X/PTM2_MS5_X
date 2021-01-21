module PTM2_MS5_X {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	
	opens view to javafx.graphics, javafx.fxml;
	opens view_model to javafx.graphics, javafx.fxml;
}
