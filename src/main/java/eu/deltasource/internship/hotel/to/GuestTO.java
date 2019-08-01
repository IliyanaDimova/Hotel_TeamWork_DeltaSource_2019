package eu.deltasource.internship.hotel.to;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;

/**
 * Guest Transfer Object - without ID
 */
public class GuestTO {
	private Gender gender;
	private String firstName;
	private String lastName;

	/**
	 * Constructor - initializes all fields
	 */
	public GuestTO(String firstName, String lastName, Gender gender) {
		this.gender = gender;
		initializeNamesAndNullChecks(firstName, lastName);
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

	/**
	 * Sets first and last name
	 * Throws Exception if null String for names
	 *
	 * @param firstName
	 * @param lastName
	 */
	private void initializeNamesAndNullChecks(String firstName, String lastName) {
		if (firstName == null || lastName == null ||
			firstName.isEmpty() || lastName.isEmpty()) {
			throw new FailedInitializationException("Guest name is invalid");
		} else if (gender == null) {
			throw new FailedInitializationException("Gender is not set");
		}
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
