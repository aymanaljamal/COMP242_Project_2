package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CentralClass extends Application {
	private FileChooser fileChooser = new FileChooser();
	static Driver driver = new Driver();

	@Override
	public void start(Stage stage) {
		Button loadButton = createButton("Load Data");
		Button Button = createButton("GO");
		Label label = createLabel("");
		Label labelInfo = createLabel("Click 'Load Data' to begin processing the file.");

		loadButton.setOnAction(event -> {
			File file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				processFile(file, label);

			} else {
				label.setText("No file selected.");
			}

		});
		Button.setOnAction(e -> MainScreen(stage));

		VBox vBox = new VBox(20, labelInfo, loadButton, Button, label);
		vBox.setAlignment(Pos.CENTER);
		vBox.setStyle("-fx-background-color: #D3D3D3;");

		Scene scene = new Scene(vBox, 500, 500);
		stage.setTitle("Data Processing ");
		stage.setScene(scene);
		stage.show();
	}

	private void processFile(File file, Label statusLabel) {
		try (Scanner scanner = new Scanner(file)) {
			int count = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				try {
					if (processLine(line)) {
						count++;
					}
				} catch (Exception e) {
					System.out.println("Error processing line: " + line);
					e.printStackTrace();
				}
			}
			statusLabel.setText(driver.districts.size() + " entries processed successfully." + count);
		} catch (FileNotFoundException e) {
			showAlert("Error ", "File not found.");
			e.printStackTrace();
		}
	}

	private boolean processLine(String line) {

		try {

			String[] info = line.split(",");
			if (info.length < 6) {
				return false;
			}

			String name = info[0].trim();
			String date = info[1].trim();
			String age = info[2].trim();
			Integer ageint = Integer.parseInt(age);
			String locationName = info[3].trim();
			String districtName = info[4].trim();
			String gender = info[5].trim();

			Martyr martyrinsert = new Martyr(name, ageint, gender);
			Martyr Martyr = driver.insertMartyr(districtName, locationName, date, martyrinsert);

			if (Martyr != null) {

				System.out.println(Martyr.toString() + "          ");

				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void MainScreen(Stage primaryStage) {

		Button btnToDistrictScreen = createButton("GO TO THE DISTRICT SCREEN");
		Button btnToLocationScreen = createButton("GO TO THE LOCATION SCREEN");
		Button btnToMartyrScreen = createButton("GO TO THE MARTYR SCREEN");
		Button writeToFile = createButton("write To File");
		btnToDistrictScreen.setOnAction(e -> {

			DistrictScreen DistrictScreen = new DistrictScreen(driver);
			DistrictScreen.start(primaryStage);
		});
		btnToLocationScreen.setOnAction(e -> {
			LocationScreen LocationScreen = new LocationScreen(driver);
			LocationScreen.start(primaryStage);
		});
		btnToMartyrScreen.setOnAction(e -> {
			MartyrScreen MartyrScreen = new MartyrScreen(driver);
			MartyrScreen.start(primaryStage);
		});
		writeToFile.setOnAction(e -> {
			driver.writeToFile(driver.districts);
		});
		VBox vbox = new VBox(10, btnToDistrictScreen, btnToLocationScreen, btnToMartyrScreen,writeToFile);
		vbox.setAlignment(Pos.CENTER);
		double buttonWidth = 250;
		double buttonHeight = 30;
		btnToDistrictScreen.setMinWidth(buttonWidth);
		btnToDistrictScreen.setMinHeight(buttonHeight);
		btnToLocationScreen.setMinWidth(buttonWidth);
		btnToLocationScreen.setMinHeight(buttonHeight);
		btnToMartyrScreen.setMinWidth(buttonWidth);
		btnToMartyrScreen.setMinHeight(buttonHeight);
		vbox.setStyle("-fx-background-color: #D3D3D3;");
		Scene scene = new Scene(vbox, 500, 500);
		primaryStage.setTitle("Main Screen");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private Button createButton(String text) {
		Button button = new Button(text);
		button.setMaxHeight(25);
		button.setMaxWidth(250);
		button.setFont(new Font("Arial", 14));
		button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

		button.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		return button;
	}

	private Label createLabel(String text) {
		Label label = new Label(text);

		label.setFont(new Font("Arial", 16));

		label.setTextFill(Color.BLACK);

		label.setPadding(new Insets(12, 24, 12, 24));

	
		return label;
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setPrefSize(400, 200);
		dialogPane.setStyle("-fx-border-color: red; " + "-fx-border-width: 2; " + "-fx-padding: 10; "
				+ "-fx-font-size: 14px; " + "-fx-font-family: 'Arial';");

		alert.showAndWait();
	}
}
