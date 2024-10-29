package application;

public class Martyr implements Comparable<Martyr> {
	private String Name;
	private Integer Age;
	private String Gender;

	public Martyr(String name, Integer age, String gender) {
		super();
		setName(name);
		setAge(age);
		setGender(gender);
	}

	public Martyr(String name) {
		super();
		setName(name);
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Integer getAge() {
		return Age;
	}

	public void setAge(Integer age) {
		Age = age;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	@Override
	public boolean equals(Object obj) {

		Martyr Martyr2 = (Martyr) obj;

		return this.Name.equalsIgnoreCase(Martyr2.getName());

	}

	@Override
	public String toString() {
		return Name + "," + Age + "," + Gender;
	}

	@Override

	public int compareTo(Martyr o) {

		if (this.getAge() != o.getAge()) {
			return this.getAge() - o.getAge();
		}

		return this.getGender().compareTo(o.getGender());
	}

}
