module COMP242_Project_2 {
	requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml  , javafx.base;
}
