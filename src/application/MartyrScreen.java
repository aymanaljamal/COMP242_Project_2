package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class MartyrScreen extends Application {
	private BinarySearchTree<District> bst;
	private BinarySearchTree<Location> bstL;
	private BinarySearchTree<MartyrDate> bstM;
	private TNode<MartyrDate> MartyrDate;
	private District district;
	private Label warningLabel = createLabel("");
	private Martyr Martyr = null;

	private Driver driver;
	private Location Location = new Location();

	public MartyrScreen(Driver driver) {
		this.driver = driver;
		this.bst = driver.districts;
		this.bstL = bst.getRoot().getData().getLocation();
		this.bstM = bstL.getRoot().getData().getMartyrDate();
		this.district = driver.districts.getRoot().getData();
		this.MartyrDate = bstM.getRoot();
		this.bstM.StackFilling();
	}

	public Location getLocation() {
		return Location;
	}

	public Martyr getMartyr() {
		return Martyr;
	}

	public void setLocation(Location location, District district) {
		this.Location = location;
		this.district = district;
		this.bstM = location.getMartyrDate();
		this.MartyrDate = bstM.getRoot();
		this.bstM.StackFilling();
	}

	public void setDistrict(TNode<District> district) {
		this.district = district.getData();
	}

	@Override
	public void start(Stage stage) {

		BorderPane mainPane = new BorderPane();

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setStyle("-fx-border-color: black;");

		Button insertButton = createButton("Insert a New Martyr");
		Button updateButton = createButton("Update a Martyr");
		Button deleteButton = createButton("Delete a Martyr");
		Button searchButton = createButton("Search for Martyrs");
		Button nextButton = createButton("Next Martyr");
		Button prevButton = createButton("Previous Martyr");

		Button writeToFileButton = createButton("Write To File");

		Label averageLabel = createLabel("Average Martyrs Ages:");
		Label youngestLabel = createLabel("The Youngest Martyrs:");
		Label oldestLabel = createLabel("The Oldest Martyrs:");
		TextField averageTextField = createTextField("=======================");
		TextField youngestTextField = createTextField("=======================");
		TextField oldestTextField = createTextField("=======================");
		Label warningLabel = new Label();

		gridPane.add(insertButton, 0, 0);
		gridPane.add(updateButton, 1, 0);
		gridPane.add(deleteButton, 2, 0);
		gridPane.add(searchButton, 3, 0);
		gridPane.add(nextButton, 2, 1);
		gridPane.add(prevButton, 1, 1);
		gridPane.add(averageLabel, 0, 2);
		gridPane.add(youngestLabel, 0, 3);
		gridPane.add(oldestLabel, 0, 4);
		gridPane.add(averageTextField, 1, 2, 3, 1);
		gridPane.add(youngestTextField, 1, 3, 3, 1);
		gridPane.add(oldestTextField, 1, 4, 3, 1);

		gridPane.add(writeToFileButton, 0, 5, 4, 1);

		HBox top = new HBox(10);
		Label locationLabel = createLabel("Location: ");
		Label districtLabel = createLabel("District: ");
		Label martyrDateLabel = createLabel("Martyr Date: ");
		top.getChildren().addAll(locationLabel, districtLabel, martyrDateLabel);
		mainPane.setTop(top);
		VBox VBox = new VBox(10);

		TableView<Martyr> tableView = new TableView<>();
		setupTableView(tableView);
		tableView.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		tableView.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));

		VBox.getChildren().addAll(top, tableView);

		mainPane.setCenter(VBox);

		insertButton.setOnAction(e -> InsertMartyr(stage));
		updateButton.setOnAction(e -> updateMartyr(stage));
		deleteButton.setOnAction(e -> deleteMartyr(stage));
		searchButton.setOnAction(e -> search(stage));

		writeToFileButton.setOnAction(e -> driver.writeToFile(driver.districts));

		nextButton.setOnAction(e -> NextButton(warningLabel, locationLabel, districtLabel, martyrDateLabel, tableView,
				averageTextField, youngestTextField, oldestTextField));
		prevButton.setOnAction(e -> PrevButton(warningLabel, locationLabel, districtLabel, martyrDateLabel, tableView,
				averageTextField, youngestTextField, oldestTextField));

		gridPane.setStyle("-fx-background-color: #D3D3D3;");
		gridPane.setAlignment(Pos.CENTER);

		mainPane.setPadding(new Insets(50, 50, 50, 50));
		mainPane.setTop(gridPane);
		mainPane.setStyle("-fx-background-color: #D3D3D3;");
		Scene scene = new Scene(mainPane, 700, 700);
		stage.setScene(scene);
		stage.setTitle("Martyr System");
		stage.show();
	}

	private void NextButton(Label warningLabel, Label locationLabel, Label districtLabel, Label martyrDateLabel,
			TableView<Martyr> tableView, TextField averageTextField, TextField youngestTextField,
			TextField oldestTextField) {
		try {
			if (bstM.isEmpty()) {
				warningLabel.setText("The list is empty.");
			} else {
				MartyrDate nextMartyrDate = bstM.getNext();
				MartyrDate.setData(nextMartyrDate);
				if (nextMartyrDate != null) {
					districtLabel.setText(district.getDistrictName());
					locationLabel.setText(Location.getLocationName());
					martyrDateLabel.setText(this.MartyrDate.getData().toString());
					ObservableList<Martyr> foundMartyrs = martyrsInfo(this.MartyrDate.getData());
					tableView.setItems(foundMartyrs);
					warningLabel.setText("Date: " + nextMartyrDate.toString());
					averageTextField.setText("" + AverageMartyrs(nextMartyrDate));
					youngestTextField.setText("" + youngest(nextMartyrDate).toString());
					oldestTextField.setText("" + oldest(nextMartyrDate).toString());
				} else {
					warningLabel.setText("This is the last district.");
				}
			}
		} catch (Exception e) {
			showAlert("Error", "The list is empty.");
		}
	}

	private void PrevButton(Label warningLabel, Label locationLabel, Label districtLabel, Label martyrDateLabel,
			TableView<Martyr> tableView, TextField averageTextField, TextField youngestTextField,
			TextField oldestTextField) {
		try {
			if (bstM.isEmpty()) {
				showAlert("Error", "The list is empty.");
			} else {
				MartyrDate prevMartyrDate = bstM.getprev();
				MartyrDate.setData(prevMartyrDate);
				if (prevMartyrDate != null) {
					districtLabel.setText(district.getDistrictName());
					locationLabel.setText(Location.getLocationName());
					martyrDateLabel.setText(this.MartyrDate.getData().toString());
					ObservableList<Martyr> foundMartyrs = martyrsInfo(this.MartyrDate.getData());
					tableView.setItems(foundMartyrs);
					warningLabel.setText("Date: " + prevMartyrDate.toString());
					averageTextField.setText("" + AverageMartyrs(prevMartyrDate));
					youngestTextField.setText("" + youngest(prevMartyrDate));
					oldestTextField.setText("" + oldest(prevMartyrDate));
				} else {
					warningLabel.setText("This is the first district.");
				}
			}
		} catch (Exception e) {
			showAlert("Error", "The list is empty.");
		}
	}

	public void InsertMartyr(Stage stage) {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		Label name = createLabel("Name:");
		TextField tfName = createTextField("Enter the name");

		Label age = createLabel("Age:");
		TextField tfAge = createTextField("Enter the Age:");

		Label dateOfDeath = createLabel("Date Of Death:");

		Label locationLabel = createLabel("Event Location:");
		ComboBox<String> cbLocation = new ComboBox<>();
		cbLocation.setPromptText("Select the Event Location");
		cbLocation.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		cbLocation.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));

		Label districtLabel = createLabel("District");
		ComboBox<String> cbDistrict = new ComboBox<>();
		cbDistrict.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		cbDistrict.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		setupDistrictComboBox(bst, cbDistrict);
		cbDistrict.setPromptText("Select the District");
		DatePicker dpDateOfDeath = new DatePicker();
		cbDistrict.setOnAction(e -> findLocation(cbDistrict.getValue(), cbLocation));

		Label lbGender = createLabel("Gender");
		ToggleGroup genderGroup = new ToggleGroup();
		RadioButton rbMale = new RadioButton("M");
		rbMale.setToggleGroup(genderGroup);
		rbMale.setSelected(true);
		RadioButton rbFemale = new RadioButton("F");
		rbFemale.setToggleGroup(genderGroup);
		VBox vboxGender = new VBox(10, rbMale, rbFemale);

		Button btInsert = createButton("Insert Martyr");
		Button btClear = createButton("Clean");
		Button btGoBack = createButton("Go Back");
		HBox hboxButtons = new HBox(10, btInsert, btClear, btGoBack);
		VBox VBox = new VBox(10);
		VBox.getChildren().addAll(hboxButtons, warningLabel);

		hboxButtons.setAlignment(Pos.CENTER);
		VBox.setAlignment(Pos.CENTER);
		grid.add(name, 0, 0);
		grid.add(tfName, 1, 0);
		grid.add(age, 0, 1);
		grid.add(tfAge, 1, 1);
		grid.add(locationLabel, 0, 2);
		grid.add(cbLocation, 1, 2);
		grid.add(dateOfDeath, 0, 3);
		grid.add(dpDateOfDeath, 1, 3);
		grid.add(districtLabel, 0, 4);
		grid.add(cbDistrict, 1, 4);
		grid.add(lbGender, 0, 5);
		grid.add(vboxGender, 1, 5);

		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(grid);
		borderPane.setBottom(VBox);
		borderPane.setStyle("-fx-background-color: #D3D3D3;");

		borderPane.setPadding(new Insets(50, 50, 50, 50));
		Scene scene = new Scene(borderPane, 500, 500);
		stage.setTitle("Insert Martyr");
		stage.setScene(scene);
		stage.show();

		btClear.setOnAction(e -> {
			warningLabel.setText("");
			tfName.clear();
			tfAge.clear();

			dpDateOfDeath.setValue(null);

			cbLocation.getItems().clear();
		});
		btGoBack.setOnAction(e -> {
			start(stage);
		});
		btInsert.setOnAction(e -> {

			String gender = "";
			if (rbMale.isSelected()) {
				gender = "M";
			} else if (rbFemale.isSelected()) {
				gender = "F";
			}

			String namee = tfName.getText().trim();

			LocalDate date = dpDateOfDeath.getValue();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
			String dateString = (date != null) ? date.format(formatter) : "";
			if (dateString.isEmpty()) {
				showAlert("Error", "No date selected.");
				return;
			}

			String ageStr = tfAge.getText().trim();
			Integer agee = null;
			try {
				agee = Integer.parseInt(ageStr);
			} catch (NumberFormatException ex) {
				showAlert("Error", "Invalid age input.");

				return;
			}

			String locationName = cbLocation.getValue();
			String districtName = cbDistrict.getValue();

			if (locationName == null || districtName == null || namee.isEmpty()) {
				showAlert("Error", "All fields must be filled.");
				return;
			}

			Martyr martyr = new Martyr(namee, agee, gender);
			try {
				Martyr insertedMartyr =driver.insertMartyr(districtName, locationName, dateString,
						martyr);
				warningLabel.setText("Martyr inserted successfully." + insertedMartyr.toString());

			} catch (Exception ex) {
				showAlert("Error", "Failed to insert martyr: " + ex.getMessage());
			}
		});

	}

	public void updateMartyr(Stage stage) {

		Button findButton = createButton("FIND");
		findButton.setOnAction(e -> {
			Martyr = deleteMartyr(stage);
		});

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		HBox topBar = new HBox(10);
		topBar.setAlignment(Pos.CENTER);
		topBar.getChildren().addAll(findButton);

		Label errorMsg = new Label();
		errorMsg.setTextFill(Color.BLACK);

		TextField tfName = createTextField("Enter the name");

		TextField tfAge = createTextField("Enter the age");

		ToggleGroup genderGroup = new ToggleGroup();
		RadioButton rbMale = new RadioButton("Male");
		RadioButton rbFemale = new RadioButton("Female");
		rbMale.setToggleGroup(genderGroup);
		rbFemale.setToggleGroup(genderGroup);

		VBox genderBox = new VBox(10, rbMale, rbFemale);

		Button clearButton = createButton("Clear");
		Button goBackButton = createButton("Go Back");
		HBox buttonBox = new HBox(10, clearButton, goBackButton, errorMsg);
		Button updateNameButton = createButton("Update Name");
		Button updateAgeButton = createButton("Update Age");
		Button updateGenderButton = createButton("Update Gender");

		grid.add(createLabel("Name:"), 0, 0);
		grid.add(tfName, 1, 0);
		grid.add(updateNameButton, 2, 0);
		grid.add(createLabel("Age:"), 0, 1);
		grid.add(tfAge, 1, 1);
		grid.add(updateAgeButton, 2, 1);

		grid.add(createLabel("Gender:"), 0, 2);
		grid.add(genderBox, 1, 2);
		grid.add(updateGenderButton, 2, 2);

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topBar);
		borderPane.setCenter(grid);
		borderPane.setBottom(buttonBox);
		borderPane.setPadding(new Insets(50));
		borderPane.setStyle("-fx-background-color: #D3D3D3;");
		Scene scene = new Scene(borderPane, 600, 400);
		stage.setTitle("Update Martyr");
		stage.setScene(scene);
		stage.show();

		clearButton.setOnAction(e -> {
			tfName.clear();
			tfAge.clear();
			genderGroup.selectToggle(rbMale);
			errorMsg.setText("");
		});

		updateNameButton.setOnAction(e -> {
			try {

				String name = tfName.getText().trim();
				Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
				confirmAlert.setTitle("Confirmation Needed");
				confirmAlert.setHeaderText("Are you sure you want to update the Name?");
				confirmAlert.setContentText("This will change the Name from " + Martyr.getName() + " to " + name + ".");
				confirmAlert.showAndWait().ifPresent(response -> {

					if (!name.isEmpty()) {
						Martyr.setName(name);
						errorMsg.setText("Name updated successfully--->" + Martyr.toString());
					} else {
						showAlert("", "Name field cannot be empty.");
					}
				});
			} catch (NumberFormatException ex) {

				showAlert("", "Invalid age input. Please enter a valid number.");
			}
		});

		updateAgeButton.setOnAction(e -> {

			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			confirmAlert.setTitle("Confirmation Needed");
			confirmAlert.setHeaderText("Are you sure you want to update the Age?");
			confirmAlert.setContentText(
					"This will change the Age from " + Martyr.getAge() + " to " + tfAge.getText().trim() + ".");
			confirmAlert.showAndWait().ifPresent(response -> {

				try {
					int age = Integer.parseInt(tfAge.getText().trim());
					if (!(age > 130 || age < 0)) {
						Martyr.setAge(age);
						errorMsg.setText("Age updated successfully--->" + Martyr.toString());
					} else {
						showAlert("", "Invalid age input. Please enter a valid number.");
					}
				} catch (NumberFormatException ex) {

					showAlert("", "Invalid age input. Please enter a valid number.");
				}
			});
		});

		updateGenderButton.setOnAction(e -> {
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			confirmAlert.setTitle("Confirmation Needed");
			confirmAlert.setHeaderText("Are you sure you want to update the Gender?");
			confirmAlert.setContentText("This will change the Gender from " + Martyr.getGender() + ".");
			confirmAlert.showAndWait().ifPresent(response -> {
				String gender = "";
				if (rbMale.isSelected()) {
					gender = "M";
				} else if (rbFemale.isSelected()) {
					gender = "F";
				}
				Martyr.setGender(gender);
				errorMsg.setText("Gender updated successfully--->" + Martyr.toString());
			});
		});

		goBackButton.setOnAction(e -> {
			start(stage);
		});
	}

	public void search(Stage stage) {
		Button findButton = createButton("FIND");
		TextField searchTextField = createTextField("");
		BorderPane root = new BorderPane();
		HBox topBar = new HBox(10);
		TableView<Martyr> tableView = new TableView<>();
		Button goBackButton = createButton("Go Back");

		setupTableView(tableView);

		topBar.getChildren().addAll(findButton, searchTextField);
		root.setTop(topBar);
		root.setCenter(tableView);
		root.setPadding(new Insets(50, 50, 50, 50));
		root.setBottom(goBackButton);
		tableView.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		tableView.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		findButton.setOnAction(e -> {

			ObservableList<Martyr> foundMartyrs = searchMartyrsByName(district.getLocation(),
					searchTextField.getText().trim());
			tableView.setItems(foundMartyrs);
		});
		goBackButton.setOnAction(e -> {
			start(stage);
		});

		root.setStyle("-fx-background-color: #D3D3D3;");
		Scene scene = new Scene(root, 600, 400);
		stage.setTitle("Search Martyrs");
		stage.setScene(scene);
		stage.show();
	}

	private void setupTableView(TableView<Martyr> tableView) {
		TableColumn<Martyr, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol.setPrefWidth(200);
		TableColumn<Martyr, Integer> ageCol = new TableColumn<>("Age");
		ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

		TableColumn<Martyr, String> genderCol = new TableColumn<>("Gender");
		genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

		tableView.getColumns().addAll(genderCol, ageCol, nameCol);

	}

	public ObservableList<Martyr> searchMartyrsByName(BinarySearchTree<Location> bstM, String searchQuery) {
		if (bstM.getRoot() == null) {
			System.out.println("No data available.");
			return FXCollections.observableArrayList();
		}

		StackUsingQueue<TNode<Location>> stack = new StackUsingQueue<>();
		stack.push(bstM.getRoot());
		ObservableList<Martyr> matchingMartyrs = FXCollections.observableArrayList();

		while (!stack.isEmpty()) {
			TNode<Location> current = stack.pop();
			BinarySearchTree<MartyrDate> martyrDates = current.getData().getMartyrDate();
			if (martyrDates.getRoot() != null) {
				StackUsingQueue<TNode<MartyrDate>> stack2 = new StackUsingQueue<>();
				stack2.push(martyrDates.getRoot());

				while (!stack2.isEmpty()) {
					TNode<MartyrDate> currentMartyrDate = stack2.pop();
					Node<Martyr> curr = currentMartyrDate.getData().getMartyrs().getHead();
					while (curr != null) {
						Martyr martyr = curr.getData();
						if (martyr.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
							matchingMartyrs.add(martyr);
						}
						curr = curr.getNext();
					}

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
		return matchingMartyrs;
	}

	public ObservableList<Martyr> martyrsInfo(MartyrDate martyrDate) {
		if (martyrDate.getMartyrs().getHead() == null) {
			showAlert("error", "No data available.");
			return FXCollections.observableArrayList();
		}

		StackUsingQueue<Node<Martyr>> stack = new StackUsingQueue<>();
		Node<Martyr> current = martyrDate.getMartyrs().getHead();

		while (current != null) {
			insertInSortedOrder(stack, current);
			current = current.getNext();
		}

		ObservableList<Martyr> matchingMartyrs = FXCollections.observableArrayList();

		while (!stack.isEmpty()) {
			matchingMartyrs.add(stack.pop().getData());
		}

		return matchingMartyrs;
	}

	private void insertInSortedOrder(StackUsingQueue<Node<Martyr>> stack, Node<Martyr> node) {
		if (!stack.isEmpty()) {
			Node<Martyr> temp = stack.pop();
			if (temp.getData().getName().compareTo(node.getData().getName()) < 0) {
				insertInSortedOrder(stack, node);
				stack.push(temp);
			} else {
				stack.push(temp);
				stack.push(node);
			}
		} else {
			stack.push(node);
		}
	}

	public double AverageMartyrs(MartyrDate martyrDate) {
		double totalAge = 0;
		double totalCount = 0;
		Node<Martyr> curr = martyrDate.getMartyrs().getHead();

		while (curr != null) {

			Martyr martyr = curr.getData();
			try {
				Integer age = martyr.getAge();
				totalAge += age;
				totalCount++;
			} catch (Exception e) {

			}
			curr = curr.getNext();
		}

		if (totalCount > 0) {
			return (totalAge / totalCount);
		} else {
			return 0;
		}

	}

	public Martyr youngest(MartyrDate martyrDate) {
		try {

			return martyrDate.getMartyrs().getHead().getData();
		} catch (Exception e) {

			return null;
		}
	}

	public Martyr oldest(MartyrDate martyrDate) {
		try {
			Node<Martyr> curr = martyrDate.getMartyrs().getHead();
			Node<Martyr> last = null;

			while (curr != null) {
				last = curr;
				curr = curr.getNext();
			}

			if (last != null) {
				return last.getData();
			}
		} catch (Exception e) {

		}
		return null;
	}

	public Martyr deleteMartyr(Stage stage) {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		HBox topBar = new HBox(10);
		topBar.setAlignment(Pos.CENTER);

		Label errorMsg = new Label();
		Button deleteButton = createButton("           Delete           ");
		Button FINDButton = createButton("           FIND           ");
		topBar.getChildren().addAll(deleteButton, FINDButton, errorMsg);

		ComboBox<String> cbDistrict = new ComboBox<>();
		cbDistrict.setPromptText("Select the District");
		cbDistrict.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		cbDistrict.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		ComboBox<String> cbLocation = new ComboBox<>();
		cbLocation.setPromptText("Select the Event Location");
		cbLocation.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		cbLocation.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		ComboBox<String> cbMartyr = new ComboBox<>();
		cbMartyr.setPromptText("Select the Martyr");
		cbMartyr.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		cbMartyr.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		ComboBox<String> cbMartyrDate = new ComboBox<>();
		cbMartyrDate.setPromptText("Select the Martyr Date");
		cbMartyrDate.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		cbMartyrDate.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		setupDistrictComboBox(bst, cbDistrict);

		cbDistrict.setOnAction(e -> findLocation(cbDistrict.getValue(), cbLocation));
		cbLocation.setOnAction(e -> findMartyrDate(cbDistrict.getValue(), cbLocation.getValue(), cbMartyrDate));
		cbMartyrDate.setOnAction(e -> {

			findMartyr(cbDistrict.getValue(), cbLocation.getValue(), cbMartyrDate.getValue(), cbMartyr);

		});

		Button clearButton = createButton("Clear");
		Button goBackButton = createButton("Go Back");
		HBox buttonBox = new HBox(10, clearButton, goBackButton);

		grid.add(createLabel("Location:"), 0, 1);
		grid.add(cbLocation, 1, 1);

		grid.add(createLabel("District:"), 0, 0);
		grid.add(cbDistrict, 1, 0);
		grid.add(createLabel("Martyr Name:"), 0, 3);
		grid.add(cbMartyr, 1, 3);
		grid.add(createLabel("Martyr Date:"), 0, 2);
		grid.add(cbMartyrDate, 1, 2);

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topBar);
		borderPane.setCenter(grid);
		borderPane.setBottom(buttonBox);
		borderPane.setPadding(new Insets(50));
		borderPane.setStyle("-fx-background-color: #D3D3D3;");
		Scene scene = new Scene(borderPane, 500, 500);
		stage.setTitle("Delete Martyr");
		stage.setScene(scene);
		stage.show();

		clearButton.setOnAction(e -> {
			errorMsg.setText("");
			cbLocation.getSelectionModel().clearSelection();
			cbDistrict.getSelectionModel().clearSelection();
			cbMartyrDate.getSelectionModel().clearSelection();
			cbMartyr.getSelectionModel().clearSelection();
		});

		deleteButton.setOnAction(e -> {
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			confirmAlert.setTitle("Confirmation Needed");
			confirmAlert.setHeaderText("Are you sure you want to delete the Martyr?");
			confirmAlert.setContentText("This will delete the Martyr .");
			confirmAlert.showAndWait().ifPresent(response -> {
				Location locationNode = driver.findLocation(cbDistrict.getValue(), cbLocation.getValue());
				if (locationNode != null) {

					MartyrDate martyrDateObj = new MartyrDate(driver.calendarTimes(cbMartyrDate.getValue()));

					TNode<MartyrDate> martyrDateNode = locationNode.getMartyrDate().find(martyrDateObj);

					if (martyrDateNode != null) {

						Martyr martyr = new Martyr(cbMartyr.getValue());

						Node<Martyr> martyrNode = martyrDateNode.getData().getMartyrs().find(martyr);

						if (martyrNode != null) {

							martyrDateNode.getData().getMartyrs().delete(martyrNode.getData());
							errorMsg.setText("Martyr deleted successfully.");
						} else {
							showAlert("", "Martyr not found.");
						}
					} else {
						showAlert("", "Martyr date not found.");
					}
				} else {
					showAlert("", "Location not found.");
				}
			});
		});
		FINDButton.setOnAction(e -> {
			Location locationNode = driver.findLocation(cbDistrict.getValue(), cbLocation.getValue());
			if (locationNode != null) {

				MartyrDate martyrDateObj = new MartyrDate(driver.calendarTimes(cbMartyrDate.getValue()));

				TNode<MartyrDate> martyrDateNode = locationNode.getMartyrDate().find(martyrDateObj);

				if (martyrDateNode != null) {

					Martyr martyr = new Martyr(cbMartyr.getValue());

					Node<Martyr> martyrNode = martyrDateNode.getData().getMartyrs().find(martyr);

					if (martyrNode != null) {
						Martyr = martyrNode.getData();

						errorMsg.setText("Martyr FINDIT successfully.");
					} else {
						showAlert("", "Martyr not found.");
					}
				} else {
					showAlert("", "Martyr date not found.");
				}
			} else {
				showAlert("", "Location not found.");
			}
		});
		goBackButton.setOnAction(e -> {
			start(stage);
		});
		clearButton.setOnAction(e -> {
			cbMartyr.getItems().clear();
			cbLocation.getItems().clear();
			cbMartyrDate.getItems().clear();
		});
		return Martyr;

	}

	public void setupDistrictComboBox(BinarySearchTree<District> bst, ComboBox<String> cbDistrict) {
		if (bst.getRoot() == null) {
			return;
		}

		LinkedListQueue<TNode<District>> queue = new LinkedListQueue<>();
		queue.enqueue(bst.getRoot());

		while (!queue.isEmpty()) {
			TNode<District> currentNode = queue.dequeue();
			cbDistrict.getItems().add(currentNode.getData().getDistrictName());

			if (currentNode.hasLeft()) {
				queue.enqueue(currentNode.getLeft());
			}
			if (currentNode.hasRight()) {
				queue.enqueue(currentNode.getRight());
			}
		}
	}

	public void findLocation(String districtName, ComboBox<String> cbLocation) {

		TNode<District> districtNode = findDistrictByName(districtName);
		if (districtNode != null) {
			setupLocationComboBox(districtNode, cbLocation);
		}
	}

	public void setupLocationComboBox(TNode<District> districtNode, ComboBox<String> cbLocation) {
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

	private void findMartyr(String district, String location, String martyrDateStr, ComboBox<String> cbMartyr) {
		Location locationNode = driver.findLocation(district, location);
		if (locationNode != null) {
			MartyrDate martyrDateObj = new MartyrDate(driver.calendarTimes(martyrDateStr));
			TNode<MartyrDate> martyrDateNode = locationNode.getMartyrDate().find(martyrDateObj);
			if (martyrDateNode != null) {
				setupMartyrComboBox(martyrDateNode.getData().getMartyrs(), cbMartyr);
			}
		}
	}

	private void setupMartyrComboBox(LinkedLists<Martyr> list, ComboBox<String> cbMartyr) {
		if (list == null || list.getHead() == null) {
			cbMartyr.setItems(FXCollections.observableArrayList());
			return;
		}
		ObservableList<String> martyrs = FXCollections.observableArrayList();
		for (Node<Martyr> currentNode = list.getHead(); currentNode != null; currentNode = currentNode.getNext()) {
			martyrs.add(currentNode.getData().getName());
		}
		cbMartyr.setItems(martyrs);
	}

	private void findMartyrDate(String district, String location, ComboBox<String> cbMartyrDate) {
		Location locationNode = driver.findLocation(district, location);
		if (locationNode != null) {
			setupMartyrDateComboBox(locationNode.getMartyrDate(), cbMartyrDate);
		}
	}

	public void setupMartyrDateComboBox(BinarySearchTree<MartyrDate> bst, ComboBox<String> cbMartyrDate) {
		if (bst.getRoot() == null) {
			cbMartyrDate.setItems(FXCollections.observableArrayList());
			return;

		}

		LinkedListQueue<TNode<MartyrDate>> queue = new LinkedListQueue<>();
		ObservableList<String> MartyrDate = FXCollections.observableArrayList();
		queue.enqueue(bst.getRoot());

		while (!queue.isEmpty()) {
			TNode<MartyrDate> currentNode = queue.dequeue();
			MartyrDate.add(currentNode.getData().toString());

			if (currentNode.hasLeft()) {
				queue.enqueue(currentNode.getLeft());
			}
			if (currentNode.hasRight()) {
				queue.enqueue(currentNode.getRight());
			}
		}
		cbMartyrDate.setItems(MartyrDate);
	}

	public TNode<District> findDistrictByName(String districtName) {

		return CentralClass.driver.districts.find(new District(districtName));
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

}
