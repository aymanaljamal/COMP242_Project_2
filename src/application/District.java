package application;

public class District implements Comparable<District> {
	private String districtName;
	protected BinarySearchTree<Location> Location = new BinarySearchTree<Location>();

	public District(String districtName) {
		super();
		setDistrictName(districtName);
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public BinarySearchTree<Location> getLocation() {
		return Location;
	}

	public void setLocation(BinarySearchTree<Location> location) {
		Location = location;
	}

	@Override
	public int compareTo(District o) {
		return o.getDistrictName().compareToIgnoreCase(districtName);
	}

	@Override
	public boolean equals(Object obj) {

		return ((District) obj).getDistrictName().equalsIgnoreCase(this.districtName);
	}

	@Override
	public String toString() {
		return districtName + ",";
	}

}
