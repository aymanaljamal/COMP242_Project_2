package application;

import javafx.application.Application;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LocationScreen extends Application {
	private BorderPane root = new BorderPane();
	private GridPane root2 = new GridPane();
	private GridPane gridPane = new GridPane();
	private Label warningLabel = createLabel("");
	private Location Location;
	private HBox hBox = new HBox(100);
	private VBox vBox = new VBox(10);
	private TNode<District> district;
	private BinarySearchTree<District> bst;
	private BinarySearchTree<Location> bstL;
	private MartyrScreen martyrScreen;

	private Driver driver;

	public LocationScreen(Driver driver) {
		this.driver = driver;
		this.bst = driver.districts;
		this.district = driver.districts.getRoot();
		this.bstL = driver.districts.getRoot().getData().getLocation();
		this.martyrScreen = new MartyrScreen(driver);
		this.Location = driver.districts.getRoot().getData().getLocation().getRoot().getData();

		bstL.StackFillingLevelByLevel();
	}

	public TNode<District> getDistrict() {
		return district;

	}

	public void setDistrict(TNode<District> district) {
		this.district = district;
		this.bstL = district.getData().getLocation();
		this.Location = district.getData().getLocation().getRoot().getData();
		bstL.StackFillingLevelByLevel();
	}

	@Override
	public void start(Stage primaryStage) {

		Button insertButton = createButton("Insert location");
		Button updateButton = createButton("Update location");
		Button deleteButton = createButton("Delete location");
		Button nextButton = createButton("    Next location  ");
		Button prevButton = createButton("Previous location");
		Button writeToFile = createButton("write To File");
		Button loadButton = createButton("  load the current location’s. ");

		Label earliestLabel = createLabel("The earliest date that has martyrs.");
		Label latestLabel = createLabel("The latest dates that has martyrs.");
		Label maximumLabel = createLabel("The date that has the maximum number of martyrs.");
		Button clearButton = createButton("clear");
		TextField insertTextField = createTextField("Enter The location name you want to add");

		TextField update2TextField = createTextField("Enter The new  location name ");

		TextField earliestTextField = createTextField("==========================");
		TextField latestTextField = createTextField("==========================");
		TextField maximumTextField = createTextField("==========================");
		ComboBox<String> comboBox = new ComboBox<>();

		comboBox.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		comboBox.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		comboBox.setValue("location name");
		ComboBox<String> comboBox2 = new ComboBox<>();
		comboBox2.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		comboBox2.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		comboBox2.setValue("location name To delete");

		ComboBox<String> updatecomboBox = new ComboBox<>();
		updatecomboBox.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		updatecomboBox.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		updatecomboBox.setValue("update District name ");
		insertButton.setOnAction(e -> {
			
			insertLocation(insertTextField.getText().trim(), driver.findDistricts(comboBox.getValue()));
			updatecomboBox.getItems().clear();
			comboBox2.getItems().clear();
			findLocation(comboBox.getValue(), comboBox2);
			findLocation(comboBox.getValue(), updatecomboBox);
			comboBox2.setValue("location name");
			updatecomboBox.setValue("location name");
		});

		updateButton.setOnAction(e -> {
			
			updateLocation(updatecomboBox.getValue(), update2TextField.getText().trim(), driver.findDistricts(comboBox.getValue()));
			updatecomboBox.getItems().clear();
			comboBox2.getItems().clear();
			findLocation(comboBox.getValue(), comboBox2);
			findLocation(comboBox.getValue(), updatecomboBox);
			comboBox2.setValue("location name");
			updatecomboBox.setValue("location name");
		});
		deleteButton.setOnAction(e -> {
			
			deleteLocation(comboBox2.getValue(), driver.findDistricts(comboBox.getValue()));
			updatecomboBox.getItems().clear();
			comboBox2.getItems().clear();
			findLocation(comboBox.getValue(), comboBox2);
			findLocation(comboBox.getValue(), updatecomboBox);
			comboBox2.setValue("location name");
			updatecomboBox.setValue("location name");
		});

		writeToFile.setOnAction(e -> {
			driver.writeToFile(driver.districts);
		});
		comboBox.setOnAction(e -> {

			findLocation(comboBox.getValue(), comboBox2);
			findLocation(comboBox.getValue(), updatecomboBox);
		});
		nextButton.setOnAction(e -> {
			try {
				if (district == null || district.getData() == null || bstL.isEmpty()) {
					showAlert("Error", "The list is empty.");
				} else {

					Location nextLocation = bstL.getNextlevl();
					this.Location = nextLocation;
					if (nextLocation != null) {

						warningLabel.setText("Location Name: " + nextLocation.getLocationName());
						earliestTextField.setText("" + earliesttDate(nextLocation.getMartyrDate()));

						latestTextField.setText("" + latesttDate(nextLocation.getMartyrDate()));
						maximumTextField.setText("" + findDateWithMaxMartyrs(nextLocation.getMartyrDate()).toString());
						bstL.printLevelByLevel(bstL.getRoot());
					} else {
						warningLabel.setText("This is the last location in the district.");
					}
				}
			} catch (Exception e2) {
				showAlert("Error", "The list is empty.");
			}
		});

		prevButton.setOnAction(e -> {
			try {
				if (district == null || district.getData() == null || bstL.isEmpty()) {
					showAlert("Error", "The list is empty.");
				} else {
					Location prevLocation = bstL.getprevlevl();
					this.Location = prevLocation;
					if (prevLocation != null) {

						warningLabel.setText("Location Name: " + prevLocation.getLocationName());
						earliestTextField.setText("" + earliesttDate(prevLocation.getMartyrDate()));

						latestTextField.setText("" + latesttDate(prevLocation.getMartyrDate()));
						maximumTextField.setText("" + findDateWithMaxMartyrs(prevLocation.getMartyrDate()).toString());
					} else {
						warningLabel.setText("This is the first location in the district.");
					}
				}
			} catch (Exception e2) {
				showAlert("Error", "The list is empty.");
			}
		});
		loadButton.setOnAction(e -> {

			martyrScreen.setLocation(Location, district.getData());
			martyrScreen.start(primaryStage);
		});
		AddToComboBox(bst.getRoot(), comboBox);

		clearButton.setOnAction(e -> {
			comboBox2.getItems().clear();
			updatecomboBox.getItems().clear();
		});
		root2.add(earliestLabel, 0, 0);
		root2.add(earliestTextField, 1, 0);
		root2.add(latestLabel, 0, 1);
		root2.add(latestTextField, 1, 1);

		root2.add(maximumLabel, 0, 2);
		root2.add(maximumTextField, 1, 2);

		hBox.getChildren().addAll(prevButton, nextButton);
		hBox.setAlignment(Pos.CENTER);

		vBox.getChildren().addAll(hBox, root2, loadButton, writeToFile, warningLabel, clearButton);
		vBox.setAlignment(Pos.CENTER);
		gridPane.add(comboBox, 0, 0);
		gridPane.add(insertButton, 0, 1);
		gridPane.add(insertTextField, 1, 1);
		gridPane.add(updateButton, 0, 2);
		gridPane.add(updatecomboBox, 1, 2);
		gridPane.add(update2TextField, 2, 2);
		gridPane.add(deleteButton, 0, 3);
		gridPane.add(comboBox2, 1, 3);

		gridPane.setHgap(10);
		gridPane.setVgap(10);
		root2.setHgap(10);
		root2.setVgap(10);
		root.setTop(gridPane);

		root.setCenter(vBox);
		root.setAlignment(root, Pos.CENTER);

		Scene scene = new Scene(root, 600, 600);
		root.setStyle("-fx-background-color: #D3D3D3;");
		root.setPadding(new Insets(60, 60, 60, 60));
		primaryStage.setScene(scene);
		primaryStage.setTitle("Location Screen");
		primaryStage.show();

	}

	public void insertLocation(String name, District district) {
		try {

			if (name == null || name.isEmpty() || district == null) {
				showAlert("Error", "Invalid input or district is not set.");
				return;
			}

			Location location = new Location(name);
			district.getLocation().insert(location);
			warningLabel.setText("Location inserted successfully.");
			bstL.StackFillingLevelByLevel();
		} catch (Exception e) {
			showAlert("Error", "Error inserting location: " + e.getMessage());

		}
	}

	public void updateLocation(String oldName, String newName, District district) {

		if (oldName == null || oldName.isEmpty() || newName == null || newName.isEmpty() || district == null) {
			showAlert("Error", "Invalid input or district not set.");
			return;
		}
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation Needed");
		confirmAlert.setHeaderText("Are you sure you want to update the Location name?");
		confirmAlert.setContentText("This will change the Location name from " + oldName + " to " + newName + ".");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				try {
					Location oldlocation = findLocation(oldName, district);
					Location newlocation = findLocation(newName, district);
					if (newlocation == null) {
						newlocation = oldlocation;
						district.getLocation().delete(oldlocation);
						newlocation.setLocationName(newName);
						district.getLocation().insert(newlocation);
						warningLabel.setText("Location updated successfully.");
						bstL.StackFillingLevelByLevel();
					} else {

						showAlert("Error", "Location not found.");
					}
				} catch (Exception e) {
					showAlert("Error", "Error updating location: " + e.getMessage());

				}
			}
		});
	}

	public void deleteLocation(String name, District district) {

		if (name == null || name.isEmpty() || district == null) {
			showAlert("Error", "Invalid input or district not set.");
			return;
		}
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation Needed");
		confirmAlert.setHeaderText("Are you sure you want to  delete the Location ?");
		confirmAlert.setContentText("This will delete Location  from  date" + name + ".");
		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				try {
					Location location = findLocation(name, district);
					if (location != null) {

						district.getLocation().delete(location);
						warningLabel.setText("Location deleted successfully.");
						bstL.StackFillingLevelByLevel();
					} else {

						showAlert("Error", "Location not found.");
					}
				} catch (Exception e) {

					showAlert("Error", "Error deleting location: " + e.getMessage());

				}
			}
		});
	}

	private void findLocation(String districtName, ComboBox<String> cbLocation) {

		TNode<District> districtNode = findDistrictByName(districtName);
		if (districtNode != null) {
			setupLocationComboBox(districtNode, cbLocation);
		}
	}

	private TNode<District> findDistrictByName(String districtName) {

		return CentralClass.driver.districts.find(new District(districtName));
	}

	private void setupLocationComboBox(TNode<District> districtNode, ComboBox<String> cbLocation) {
		if (districtNode == null || districtNode.getData().getLocation().getRoot() == null) {
			return;
		}

		BinarySearchTree<Location> bst = districtNode.getData().getLocation();
		LinkedListQueue<TNode<Location>> queue = new LinkedListQueue<>();
		queue.enqueue(bst.getRoot());

		while (!queue.isEmpty()) {
			TNode<Location> currentNode = queue.dequeue();
			cbLocation.getItems().add(currentNode.getData().getLocationName());

			if (currentNode.hasLeft()) {
				queue.enqueue(currentNode.getLeft());
			}
			if (currentNode.hasRight()) {
				queue.enqueue(currentNode.getRight());
			}
		}
	}

	private void AddToComboBox(TNode<District> root, ComboBox<String> ComboBox) {
		if (root == null)
			return;

		LinkedListQueue<TNode<District>> nodeQueue = new LinkedListQueue<>();
		nodeQueue.enqueue(root);
		;

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

	public Location findLocation(String name, District district) {

		if (name == null || name.isEmpty()) {
			return null;
		}

		Location searchLocation = new Location(name);

		TNode<Location> foundLocationNode = district.getLocation().find(searchLocation);

		if (foundLocationNode != null) {
			return foundLocationNode.getData();
		}

		return null;
	}

	public MartyrDate findDateWithMaxMartyrs(BinarySearchTree<MartyrDate> bstM) {
		if (bstM == null || bstM.getRoot() == null) {
			return null;
		}

		TNode<MartyrDate> maxNode = findMaxNode(bstM.getRoot(), bstM.getRoot(),
				bstM.getRoot().getData().getMartyrs().length());
		return maxNode == null ? null : maxNode.getData();
	}

	private TNode<MartyrDate> findMaxNode(TNode<MartyrDate> current, TNode<MartyrDate> maxNode, int maxSize) {
		if (current == null) {
			return maxNode;
		}

		int currentSize = current.getData().getMartyrs().length();

		if (currentSize > maxSize) {
			maxSize = currentSize;
			maxNode = current;
		}

		maxNode = findMaxNode(current.getLeft(), maxNode, maxSize);
		maxNode = findMaxNode(current.getRight(), maxNode, maxSize);

		return maxNode;
	}

	private MartyrDate latesttDate(BinarySearchTree<MartyrDate> bstM) {

		if (bstM == null || bstM.getRoot() == null) {
			return null;
		}
		TNode<MartyrDate> curr = bstM.getRoot();

		while (curr != null && curr.hasLeft()) {
			curr = curr.getLeft();
		}
		return curr == null ? null : curr.getData();
	}

	private MartyrDate earliesttDate(BinarySearchTree<MartyrDate> bstM) {
		if (bstM == null || bstM.getRoot() == null) {
			return null;
		}
		TNode<MartyrDate> curr = bstM.getRoot();

		while (curr != null && curr.hasRight()) {
			curr = curr.getRight();
		}
		return curr == null ? null : curr.getData();
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

	public void setDistrict(District district2) {
		district.setData(district2);

	}

}
