
package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DistrictScreen extends Application {
	private BorderPane root = new BorderPane();
	private GridPane gridPane = new GridPane();
	private Label warningLabel = createLabel("");
	private HBox hBox = new HBox(100);
	private HBox hBox2 = new HBox(10);
	private VBox vBox = new VBox(20);

	private BinarySearchTree<District> bst;
	private TNode<District> district;
	private Driver driver;
	private LocationScreen locationScreen;

	public DistrictScreen(Driver driver) {
		this.driver = driver;
		this.bst = driver.districts;
		this.district = bst.getRoot();
		this.locationScreen = new LocationScreen(driver);
		driver.districts.StackFilling();
		driver.districts.printLevelByLevel(district);
	}

	@Override
	public void start(Stage primaryStage) {

		Button insertButton = createButton("Insert District");
		Button updateButton = createButton("Update District");
		Button deleteButton = createButton("Delete District");
		Button nextButton = createButton("    Next District  ");
		Button prevButton = createButton("Previous District");
		Button writeToFile = createButton("write To File");
		Button loadButton = createButton(" load the current district’s ");
		Label numberMartyrsLabel = createLabel("Total number of martyrs");
		TextField insertTextField = createTextField("Enter The District name you want to add");

		TextField update2TextField = createTextField("Enter The new  District name ");

		TextField numberMartyrsTextField = createTextField("=============== ");
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		comboBox.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		comboBox.setValue("District name To delete");

		ComboBox<String> updatecomboBox = new ComboBox<>();
		updatecomboBox.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		updatecomboBox.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		updatecomboBox.setValue("update District name ");
		AddToComboBox(bst.getRoot(), comboBox);
		AddToComboBox(bst.getRoot(), updatecomboBox);
		insertButton.setOnAction(e -> {
			insertDistrict(insertTextField.getText().trim());
			comboBox.getItems().clear();
			updatecomboBox.getItems().clear();
			AddToComboBox(bst.getRoot(), comboBox);
			AddToComboBox(bst.getRoot(), updatecomboBox);
		});
		updateButton.setOnAction(e -> {
			updateDistrict(updatecomboBox.getValue(), update2TextField.getText().trim());
			comboBox.getItems().clear();
			updatecomboBox.getItems().clear();
			AddToComboBox(bst.getRoot(), comboBox);
			AddToComboBox(bst.getRoot(), updatecomboBox);
		});
		deleteButton.setOnAction(e -> {
			deleteDistrict(comboBox.getValue());
			comboBox.getItems().clear();
			updatecomboBox.getItems().clear();
			AddToComboBox(bst.getRoot(), comboBox);
			AddToComboBox(bst.getRoot(), updatecomboBox);
		});

		loadButton.setOnAction(e -> {
			locationScreen.setDistrict(district);
			locationScreen.start(primaryStage);
		});
		nextButton.setOnAction(e -> {
			try {

				if (driver.districts.isEmpty()) {
					showAlert("Error", "The list is empty.");
				} else {
					District nextDistrict = bst.getNext();
					if (nextDistrict != null) {
						district = new TNode<>(nextDistrict);
						warningLabel.setText("District Name: " + district.getData().getDistrictName());
						numberMartyrsTextField.setText("" + totalNumberMartyrs(district.getData().getLocation()));
					} else {
						warningLabel.setText("This is the last district.");
					}
				}
			} catch (Exception e2) {
				showAlert("Error", "The list is empty.");
			}
		});

		prevButton.setOnAction(e -> {
			if (driver.districts.isEmpty()) {
				showAlert("Error", "The list is empty.");
			} else {
				District prevDistrict = bst.getprev();
				if (prevDistrict != null) {
					district.setData(prevDistrict);
					warningLabel.setText("District Name: " + district.getData().getDistrictName());
					numberMartyrsTextField.setText("" + totalNumberMartyrs(district.getData().getLocation()));
				} else {
					warningLabel.setText("This is the first district.");
				}
			}
		});
		Button clearButton = createButton("clear");
		clearButton.setOnAction(e -> {
			comboBox.getItems().clear();
			updatecomboBox.getItems().clear();
			AddToComboBox(bst.getRoot(), comboBox);
			AddToComboBox(bst.getRoot(), updatecomboBox);
		});
		writeToFile.setOnAction(e -> {
			driver.writeToFile(driver.districts);
		});
		hBox.getChildren().addAll(prevButton, nextButton);
		hBox.setAlignment(Pos.CENTER);
		hBox2.getChildren().addAll(numberMartyrsLabel, numberMartyrsTextField);
		vBox.getChildren().addAll(hBox, hBox2, loadButton, writeToFile);
		vBox.setAlignment(Pos.CENTER);
		hBox2.setAlignment(Pos.CENTER);
		gridPane.add(insertButton, 0, 0);
		gridPane.add(insertTextField, 1, 0);
		gridPane.add(updateButton, 0, 1);
		gridPane.add(updatecomboBox, 1, 1);
		gridPane.add(update2TextField, 2, 1);
		gridPane.add(deleteButton, 0, 2);
		gridPane.add(comboBox, 1, 2);

		gridPane.setHgap(10);
		gridPane.setVgap(10);
		root.setTop(gridPane);
		root.setBottom(warningLabel);
		root.setCenter(vBox);
		root.setAlignment(root, Pos.CENTER);
		root.setAlignment(warningLabel, Pos.CENTER);
		gridPane.setPadding(new Insets(40, 40, 40, 40));
		Scene scene = new Scene(root, 700, 500);
		root.setStyle("-fx-background-color: #D3D3D3;");

		root.setPadding(new Insets(40, 40, 40, 40));
		primaryStage.setScene(scene);
		primaryStage.setTitle("District Screen");
		primaryStage.show();
	}

	private void insertDistrict(String name) {

		if (name == null || name.isEmpty()) {
			showAlert("Error", "Invalid District name.");
			return;
		}

		try {

			District districtFound = CentralClass.driver.findDistricts(name);
			if (districtFound != null) {
				showAlert("Error", "District found: " + districtFound);
				return;

			}
			District district = CentralClass.driver.insertDistrict(name);
			if (district != null) {
				warningLabel.setText("Inserting district: " + district);
				driver.districts.StackFilling();

			} else {
				showAlert("Error", "Failed to insert new district.");
			}
		} catch (Exception e) {
			showAlert("Error", "Error processing the request.");
		}
	}

	private void updateDistrict(String oldName, String newName) {
		if (oldName == null || newName == null || oldName.isEmpty() || newName.isEmpty()) {
			showAlert("Error", "Please provide valid district names.");
			return;
		}
		if (CentralClass.driver.findDistricts(newName) != null) {
			showAlert("Error", "The new district name already exists.");
			return;
		}

		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation Needed");
		confirmAlert.setHeaderText("Are you sure you want to update the district name?");
		confirmAlert.setContentText("This will change the district name from " + oldName + " to " + newName + ".");

		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				try {
					District districtFound = CentralClass.driver.findDistricts(oldName);
					if (districtFound != null) {
						districtFound.setDistrictName(newName);
						CentralClass.driver.districts.insert(districtFound);
						driver.districts.StackFilling();
						warningLabel.setText("District updated successfully.");
					} else {
						showAlert("Error", "District not found.");
					}
				} catch (Exception e) {
					showAlert("Error", "An error occurred while updating the district.");
					e.printStackTrace();
				}
			} else {
				warningLabel.setText("Update cancelled.");
			}
		});
	}

	private void deleteDistrict(String name) {

		if (name == null || name.isEmpty()) {
			showAlert("Error", "Invalid District name.");
			return;
		}

		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation Needed");
		confirmAlert.setHeaderText("Are you sure you want to  delete the district ?");
		confirmAlert.setContentText("This will delete District  from  date" + name + ".");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				try {
					District districtFound = CentralClass.driver.findDistricts(name);
					if (CentralClass.driver.districts.delete(districtFound) != null) {
						warningLabel.setText("delete it: " + districtFound);
						driver.districts.StackFilling();
						return;

					} else {
						showAlert("Error", "Failed to delete district.");
					}
				} catch (Exception e) {
					showAlert("Error", "Error processing the request." + e.toString());
				}
			}
		});
	}

	public int totalNumberMartyrs(BinarySearchTree<Location> bstM) {

		if (bstM.getRoot() == null) {
			showAlert("Error", "No data available.");
			return 0;
		}

		Stack<TNode<Location>> stack = new Stack<>();
		stack.push(bstM.getRoot());
		int totalMartyrs = 0;

		while (!stack.isEmpty()) {
			TNode<Location> current = stack.pop();
			System.out.println(current.getData());

			BinarySearchTree<MartyrDate> martyrDates = current.getData().getMartyrDate();
			if (martyrDates.getRoot() != null) {
				Stack<TNode<MartyrDate>> stack2 = new Stack<>();
				stack2.push(martyrDates.getRoot());

				while (!stack2.isEmpty()) {
					TNode<MartyrDate> currentMartyrDate = stack2.pop();
					System.out.println(currentMartyrDate.getData());

					totalMartyrs += currentMartyrDate.getData().getMartyrs().length();

					if (currentMartyrDate.getLeft() != null) {
						stack2.push(currentMartyrDate.getLeft());
					}
					if (currentMartyrDate.getRight() != null) {
						stack2.push(currentMartyrDate.getRight());
					}
				}
			}

			if (current.getLeft() != null) {
				stack.push(current.getLeft());
			}
			if (current.getRight() != null) {
				stack.push(current.getRight());
			}
		}
		return totalMartyrs;
	}

	private void AddToComboBox(TNode<District> root, ComboBox<String> ComboBox) {
		if (root == null)
			return;

		LinkedListQueue<TNode<District>> nodeQueue = new LinkedListQueue<>();
		nodeQueue.enqueue(root);
		
		while (!nodeQueue.isEmpty()) {
			TNode<District> currentNode = nodeQueue.dequeue();
			ComboBox.getItems().addAll(currentNode.getData().getDistrictName());
			if (currentNode.getLeft() != null) {
				nodeQueue.enqueue(currentNode.getLeft());
			}
			if (currentNode.getRight() != null) {
				nodeQueue.enqueue(currentNode.getRight());
			}
		}
	}

	private TextField createTextField(String PromptText) {
		TextField textField = new TextField();
		textField.setMaxHeight(25);
		textField.setMaxWidth(350);
		textField.setPromptText(PromptText);
		textField.setFont(new Font("Arial", 14));

		textField.setStyle("-fx-background-color: #FFFFFF; " + "-fx-background-radius: 10; " + "-fx-border-radius: 10; "
				+ "-fx-border-color: #B0C4DE; " + "-fx-padding: 5;");

		return textField;
	}

	private Label createLabel(String text) {
		Label label = new Label(text);

		label.setFont(new Font("Arial", 16));

		label.setTextFill(Color.BLACK);

		label.setPadding(new Insets(12, 24, 12, 24));

		return label;
	}

	private Button createButton(String text) {
		Button button = new Button(text);
		button.setMaxHeight(25);
		button.setMaxWidth(250);
		button.setFont(new Font("Arial", 14));
		button.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		button.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		return button;
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
