package application;

public class Location implements Comparable<Location> {
	private String LocationName;
	private BinarySearchTree<MartyrDate> MartyrDate = new BinarySearchTree<MartyrDate>();

	public Location(String locationName) {
		super();
		setLocationName(locationName);
	}

	public Location() {
		
	}

	public String getLocationName() {
		return LocationName;
	}

	public void setLocationName(String locationName) {
		LocationName = locationName;
	}

	
	public BinarySearchTree<MartyrDate> getMartyrDate() {
		return MartyrDate;
	}

	public void setMartyrDate(BinarySearchTree<MartyrDate> martyrDate) {
		MartyrDate = martyrDate;
	}

	@Override
	public String toString() {
		return LocationName + ",";
	}

	@Override
	public int compareTo(Location o) {

		String DistrictName1 = this.LocationName.replaceAll("'", "");
		String DistrictName2 = o.LocationName.replaceAll("'", "");
		return DistrictName1.compareToIgnoreCase(DistrictName2);

	}

	@Override
	public boolean equals(Object obj) {

		return ((Location) obj).getLocationName().equalsIgnoreCase(this.LocationName);
	}

}
