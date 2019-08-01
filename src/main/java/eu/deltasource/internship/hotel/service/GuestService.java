package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.to.GuestTO;

import java.util.List;

/**
 * Created by Taner Ilyazov - Delta Source Bulgaria on 2019-07-28.
 */
public class GuestService {

	private final GuestRepository guestRepository;

	public GuestService(GuestRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	/**
	 * Checks if guest by given ID exists
	 *
	 * @param id given ID
	 * @return true if the guest exists
	 */
	public boolean existsById(int id) {
		return guestRepository.existsById(id);
	}

	/**
	 * Adds a new guest to GuestRepository
	 *
	 * @param guest transfer object for guest
	 */
	public void createGuest(GuestTO guest) {
		Guest newGuest = new Guest(0, guest.getLastName(), guest.getFirstName(), guest.getGender());
		guestRepository.save(newGuest);
	}

	/**
	 * @return A list of all guests in a GuestRepository
	 */
	public List<Guest> returnAllGuests() {
		return guestRepository.findAll();
	}

	/**
	 * Returns a guest by given guest ID
	 * Throws Exception if Guest with this ID doesn't exist!
	 *
	 * @param guestId given guest ID
	 * @return The guest wit this ID
	 */
	public Guest returnGuestById(int guestId) {
		return guestRepository.findById(guestId);
	}

	/**
	 * Updates a guest's name and gender by given guest ID
	 * Throws Exception if Guest with this ID doesn't exist!
	 *
	 * @param guestId   the ID of the guest we want to update
	 * @param firstName to be updated
	 * @param lastName  to be updated
	 * @param gender    to be updated
	 */
	public void updateGuestById(int guestId, String firstName, String lastName, Gender gender) {
		Guest updatedGuest = new Guest(guestId, firstName, lastName, gender);
		guestRepository.updateGuest(updatedGuest);
	}

	/**
	 * Removes a guest by ID
	 * Throws Exception if Guest with this ID doesn't exist!
	 *
	 * @param guestId the ID of the guest we want to remove
	 */
	public void removeGuestById(int guestId) {
		guestRepository.delete(returnGuestById(guestId));
	}
}
