package eu.deltasource.internship.hotel.to;

import eu.deltasource.internship.hotel.domain.Gender;

/**
 * Guest Transfer Object - without ID
 */
public class GuestTO {
	private Gender gender;
	private String firstName;
	private String lastName;

	/**
	 * Constructor - initializes all fields without any validation (validation is done when you use the
	 * transfer object to generate Guest with ID)
	 */
	public GuestTO(String firstName, String lastName, Gender gender) {
		this.gender = gender;
		this.firstName = firstName;
		this.lastName = lastName;;
	}

	public Gender getGender() {
		return gender;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
