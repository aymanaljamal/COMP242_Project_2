package application;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Driver {
	static BinarySearchTree<District> districts = new BinarySearchTree<District>();

	public District findDistricts(String nameDistricts) {
		if (nameDistricts == null || nameDistricts.isEmpty()) {
			return null;
		}
		District districtsSearch = new District(nameDistricts);
		TNode<District> node = districts.find(districtsSearch);
		if (node == null) {
			return null;
		}
		return node.getData();
	}

	public District insertDistrict(String nameDistricts) {
		if (nameDistricts == null || nameDistricts.isEmpty()) {
			return null;
		}
		District districtsSearch = new District(nameDistricts);
		District foundNode = findDistricts(nameDistricts);
		if (foundNode == null) {
			districts.insert(districtsSearch);
			return districtsSearch;
		}
		return foundNode;
	}

	public Location findLocation(String nameDistricts, String nameLocation) {
		if (nameDistricts == null || nameDistricts.isEmpty() || nameLocation == null || nameLocation.isEmpty()) {
			return null;
		}
		District districtss = findDistricts(nameDistricts);
		if (districtss == null) {
			return null;
		}
		Location LocationSearch = new Location(nameLocation);
		TNode<Location> node = districtss.getLocation().find(LocationSearch);
		if (node == null) {

			return null;
		}

		return node.getData();
	}

	public Location insertLocation(String nameDistricts, String nameLocation) {
		if (nameDistricts == null || nameDistricts.isEmpty() || nameLocation == null || nameLocation.isEmpty()) {
			return null;
		}
		District districtss = insertDistrict(nameDistricts);
		if (districtss == null) {
			return null;
		}
		Location foundNode = findLocation(nameDistricts, nameLocation);
		Location Location = new Location(nameLocation);
		if (foundNode == null) {
			districtss.getLocation().insert(Location);
			return Location;
		}
		return foundNode;
	}

	public MartyrDate findMartyrDate(String districtName, String locationName, String martyrDateString) {
		District district = findDistricts(districtName);
		if (district == null) {
			return null;
		}
		Location location = findLocation(districtName, locationName);
		if (location == null) {
			return null;
		}
		Calendar calendar = calendarTimes(martyrDateString);
		if (calendar == null) {
			return null;
		}
		MartyrDate martyrDate = new MartyrDate(calendar);
		TNode<MartyrDate> node = location.getMartyrDate().find(martyrDate);
		return (node != null) ? node.getData() : null;
	}

	public MartyrDate insertMartyrDate(String districtName, String locationName, String martyrDateString) {
		District district = insertDistrict(districtName);
		if (district == null) {
			return null;
		}

		Location location = insertLocation(districtName, locationName);
		if (location == null) {
			return null;
		}
		Calendar calendar = calendarTimes(martyrDateString);
		if (calendar == null) {
			return null;
		}
		MartyrDate martyrDate = new MartyrDate(calendar);
		MartyrDate foundMartyrDate = findMartyrDate(districtName, locationName, martyrDateString);
		if (foundMartyrDate == null) {
			location.getMartyrDate().insert(martyrDate);
			return martyrDate;
		}
		return foundMartyrDate;
	}

	public Martyr findMartyr(String districtName, String locationName, String martyrDateString, Martyr martyr) {
		MartyrDate MartyrDate = findMartyrDate(districtName, locationName, martyrDateString);
		if (MartyrDate == null) {
			return null;
		}
		if (martyr == null) {
			return null;
		}
		Node<Martyr> node = MartyrDate.getMartyrs().find(martyr);
		if (node == null) {
			return null;
		}
		return node.getData();
	}

	public Martyr insertMartyr(String districtName, String locationName, String martyrDateString, Martyr martyr) {
		MartyrDate MartyrDate = insertMartyrDate(districtName, locationName, martyrDateString);
		if (MartyrDate == null) {
			return null;
		}
		if (martyr == null) {
			return null;
		}
		Martyr foundNode = findMartyr(districtName, locationName, martyrDateString, martyr);
		if (foundNode == null) {
			MartyrDate.getMartyrs().insert(martyr);
			return martyr;
		}
		return foundNode;
	}

	public Martyr deleteMartyr(String districtName, String locationName, String martyrDateString, Martyr martyr) {

		MartyrDate martyrDate = insertMartyrDate(districtName, locationName, martyrDateString);
		if (martyrDate == null || martyr == null) {

			return null;
		}

		Martyr foundNode = findMartyr(districtName, locationName, martyrDateString, martyr);
		if (foundNode != null) {

			Node<Martyr> isDeleted = martyrDate.getMartyrs().delete(foundNode);
			if (isDeleted != null) {

				return foundNode;
			} else {

				return null;
			}
		}

		return null;
	}

	public static Calendar calendarTimes(String timeString) {
		try {
			String[] time = timeString.split("[,:/ |#]");

			if (time.length < 3) {
				throw new IllegalArgumentException(
						"Input string does not contain enough data. Expected format: 'month,day,year'");
			}

			int year = Integer.parseInt(time[2].trim());
			int num1 = Integer.parseInt(time[0].trim());
			int month = num1 - 1;
			int day = Integer.parseInt(time[1].trim());
			Calendar calendar = new GregorianCalendar(year, month, day);

			return calendar;

		} catch (NumberFormatException e) {
			System.out.println("Number format exception: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Illegal argument exception: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
		return null;
	}




    public void writeToFile(BinarySearchTree<District> districts) {
        String filePath = "Data.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Name,Date,Age,Location,District,Gender\n");

            TNode<District> nodeDistrict = districts.getRoot();
            if (nodeDistrict == null) {
                System.out.println("No district data available.");
                return;
            }

            StackUsingQueue<TNode<District>> stackDistrict = new StackUsingQueue<>();
            stackDistrict.push(nodeDistrict);

            while (!stackDistrict.isEmpty()) {
                TNode<District> currentDistrict = stackDistrict.pop();

                BinarySearchTree<Location> locations = currentDistrict.getData().getLocation();
                TNode<Location> nodeLocation = locations.getRoot();
                if (nodeLocation == null) {
                    continue;
                }

                StackUsingQueue<TNode<Location>> stackLocation = new StackUsingQueue<>();
                stackLocation.push(nodeLocation);

                while (!stackLocation.isEmpty()) {
                    TNode<Location> currentLocation = stackLocation.pop();

                    BinarySearchTree<MartyrDate> martyrDates = currentLocation.getData().getMartyrDate();
                    TNode<MartyrDate> nodeMartyrDate = martyrDates.getRoot();

                    if (nodeMartyrDate != null) {
                        StackUsingQueue<TNode<MartyrDate>> stackMartyrDate = new StackUsingQueue<>();
                        stackMartyrDate.push(nodeMartyrDate);

                        while (!stackMartyrDate.isEmpty()) {
                            TNode<MartyrDate> currentMartyrDate = stackMartyrDate.pop();
                            Node<Martyr> curr = currentMartyrDate.getData().getMartyrs().getHead();

                            while (curr != null) {
                                Martyr martyr = curr.getData();
                                writer.append(martyr.getName() + "," + currentMartyrDate.getData().toString() + ","
                                        + martyr.getAge() + "," + currentLocation.getData().getLocationName() + ","
                                        + currentDistrict.getData().getDistrictName() + "," + martyr.getGender() + "\n");
                                curr = curr.getNext();
                            }

                            if (currentMartyrDate.getLeft() != null) {
                                stackMartyrDate.push(currentMartyrDate.getLeft());
                            }
                            if (currentMartyrDate.getRight() != null) {
                                stackMartyrDate.push(currentMartyrDate.getRight());
                            }
                        }
                    }

                    if (currentLocation.getLeft() != null) {
                        stackLocation.push(currentLocation.getLeft());
                    }
                    if (currentLocation.getRight() != null) {
                        stackLocation.push(currentLocation.getRight());
                    }
                }

                if (currentDistrict.getLeft() != null) {
                    stackDistrict.push(currentDistrict.getLeft());
                }
                if (currentDistrict.getRight() != null) {
                    stackDistrict.push(currentDistrict.getRight());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
